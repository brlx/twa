package hu.brlx.tinyweatheralert.logic.auth;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import hu.brlx.tinyweatheralert.persistence.Dao;
import hu.brlx.tinyweatheralert.persistence.entity.User;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class AuthService {

    private static final Logger LOG = Logger.getLogger(AuthService.class.getName());

    private static final String LOGIN_ERROR_MSG = "Invalid username or password.";
    private static final String AUTH_SESSION_EXPIRED_MSG = "Session expired, please login.";

    private final Map<String, User> userMap = new HashMap<>();

    private Gson gson;

    @Inject
    Dao dao;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    /**
     * If the username and password are correct, updates the last login date for the user,
     * and creates and returns a cookie for them, which can be used to access the REST API.
     */
    public LoginResponse login(String userName, String password) {
        LOG.fine("login, userName=" + userName);
        final User user = dao.findUserByName(userName);
        if (user == null || !user.getPassword().equals(password)) {
            return LoginResponse.failed(LOGIN_ERROR_MSG);
        }
        final Date now = new Date();
        user.setLastLogin(now);
        user.setCookie(generateCookie(user.getUserName(), now));
        final User savedUser = dao.save(user);
        userMap.put(savedUser.getUserName(), savedUser);
        return LoginResponse.success(user);
    }

    /**
     * Checks whether the provided cookie is still valid for that user.
     *
     * @param clientCookieRaw The raw client cookie
     * @return
     */
    public AuthResponse authorize(String clientCookieRaw) {
        LOG.fine("authorize, clientCookieRaw='" + clientCookieRaw + "'");
        final ClientCookie clientCookie;
        try {
            clientCookie = gson.fromJson(clientCookieRaw, ClientCookie.class);
        } catch (JsonSyntaxException jsonE) {
            LOG.info("Invalid cookie received: " + clientCookieRaw + ", error: " + jsonE.getClass() + ": " + jsonE.getMessage());
            return AuthResponse.failed("Invalid cookie");
        }
        return authorize(clientCookie.getUserName(), clientCookie.getToken());
    }

    public AuthResponse authorize(String userName, String token) {
        final User cachedUser = userMap.get(userName);
        if (cachedUser != null) {
            // if we have a user cached here, it *is* the latest and valid
            if (cachedUser.getCookie().equals(token)) {
                LOG.fine("authorize, user found in the cache and cookie DOES match, login SUCCESSFUL");
                return AuthResponse.success(cachedUser);
            } else {
                LOG.fine("authorize, user found in the cache and cookie does not match");
                return AuthResponse.failed(AUTH_SESSION_EXPIRED_MSG);
            }
        } else {
            // we have to fetch the user
            final User user = dao.findUserByName(userName);
            if (user == null) {
                LOG.fine("authorize, user NOT found in the cache, and userName does NOT exist");
                return AuthResponse.failed(AUTH_SESSION_EXPIRED_MSG);
            }
            if (user.getCookie() == null || !user.getCookie().equals(token)) {
                LOG.fine("authorize, user NOT found in the cache, and cookie does NOT match");
                return AuthResponse.failed(AUTH_SESSION_EXPIRED_MSG);
            } else {
                LOG.fine("authorize, user NOT found in the cache, and cookie DOES match, login SUCCESSFUL");
                userMap.put(user.getUserName(), user);
                return AuthResponse.success(user);
            }
        }
    }

    public LogoutResponse logout(String clientCookieRaw) {
        LOG.fine("logout, clientCookieRaw=" + clientCookieRaw);
        // fetch the user if found and then null its cookie
        final ClientCookie clientCookie;
        try {
            clientCookie = gson.fromJson(clientCookieRaw, ClientCookie.class);
        } catch (JsonSyntaxException jsonE) {
            LOG.info("Invalid cookie received: " + clientCookieRaw + ", error: " + jsonE.getClass() + ": " + jsonE.getMessage());
            return LogoutResponse.failed("Invalid cookie");
        }

        final User user = dao.findUserByName(clientCookie.getUserName());
        if (user == null) {
            // we don't show whether the user exists or not
            return LogoutResponse.success();
        }
        user.setCookie("LOGGEDOUT");
        userMap.remove(user.getUserName());
        dao.save(user);
        return LogoutResponse.success();
    }

    public static String generateCookie(String userName, Date loginDate) {
        // TODO generateCookie
        return userName + "-" + loginDate.getTime();
    }

}
