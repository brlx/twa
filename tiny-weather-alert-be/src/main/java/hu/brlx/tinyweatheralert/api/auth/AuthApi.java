package hu.brlx.tinyweatheralert.api.auth;

import com.google.gson.Gson;
import hu.brlx.tinyweatheralert.api.AuthFilter;
import hu.brlx.tinyweatheralert.api.JAXRSConfiguration;
import hu.brlx.tinyweatheralert.logic.auth.AuthService;
import hu.brlx.tinyweatheralert.logic.auth.LoginResponse;
import hu.brlx.tinyweatheralert.logic.auth.LogoutResponse;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

@Path("auth")
@Stateless
public class AuthApi {

    private static final Logger LOG = Logger.getLogger(AuthApi.class.getName());

    private Gson gson;

    @Inject
    private AuthService service;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@QueryParam("username") String userName,
                         @QueryParam("password") String password) {
        LOG.fine("login, username=" + userName);
        final LoginResponse loginResponse = service.login(userName, password);
        return gson.toJson(loginResponse);
    }

    @GET
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public String logout(@Context HttpHeaders headers) {
        LOG.fine("logout");
        final List<String> authHeadersList = headers.getRequestHeader(AuthFilter.HEADER_NAME_AUTH);
        if (authHeadersList == null || authHeadersList.size() == 0) {
            return gson.toJson(LogoutResponse.failed("Invalid authHeader"));
        }

        final LogoutResponse logoutResp = service.logout(authHeadersList.get(0));
        return gson.toJson(logoutResp);
    }

    @OPTIONS
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutOPTIONS() {
        return "{\"SUCCESS\": true}";
    }
}
