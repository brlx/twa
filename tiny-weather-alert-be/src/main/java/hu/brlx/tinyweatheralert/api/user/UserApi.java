package hu.brlx.tinyweatheralert.api.user;

import com.google.gson.Gson;
import hu.brlx.tinyweatheralert.logic.users.UserResponse;
import hu.brlx.tinyweatheralert.logic.users.UserService;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("users")
@Stateless
public class UserApi {

    private static final Logger LOG = Logger.getLogger(UserApi.class.getName());

    private Gson gson;

    @Inject
    private UserService service;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    @GET
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public String createUser(@QueryParam("username") String username,
                             @QueryParam("password") String password) {
        LOG.fine("createUser, username=" + username + ", password=" + password);

        final UserResponse userResponse = service.createUser(username, password);

        return gson.toJson(userResponse);
    }

}
