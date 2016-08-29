package hu.brlx.tinyweatheralert.logic.auth;

import hu.brlx.tinyweatheralert.logic.Response;
import hu.brlx.tinyweatheralert.persistence.entity.User;

/**
 * Response for the client when it tries to login and request a new access cookie.
 */
public class LoginResponse extends Response {

    private ClientCookie cookie;

    public static LoginResponse success(User user) {
        final LoginResponse resp = new LoginResponse();
        resp.success = true;
        resp.cookie = new ClientCookie(user);
        return resp;
    }

    public static LoginResponse failed(String message) {
        final LoginResponse resp = new LoginResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }

    public ClientCookie getCookie() {
        return cookie;
    }
}
