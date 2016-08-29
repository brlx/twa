package hu.brlx.tinyweatheralert.api;

import hu.brlx.tinyweatheralert.logic.auth.AuthResponse;
import hu.brlx.tinyweatheralert.logic.auth.AuthService;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Provider
public class AuthFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(AuthFilter.class.getName());

    private static final String PATH_AUTH_LOGIN = "/auth/login";
    private static final String PATH_DEV = "/dev";
    private static final String REST_METHOD_NAME_OPTIONS = "OPTIONS";
    public static final String HEADER_NAME_AUTH = "TWA-auth";
    public static final String HEADER_NAME_LOGGEDINUSER = "TWA-loggedinuser";

    @Inject
    private AuthService service;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        LOG.fine("filter, " + ctx.getMethod() + " " + ctx.getUriInfo().getAbsolutePath() + " " + ctx.getUriInfo().getPath());

        if (ctx.getUriInfo().getPath().equals(PATH_AUTH_LOGIN)) {
            LOG.fine("GET login, skipping header checking");
            return;
        }

        if (ctx.getUriInfo().getPath().startsWith(PATH_DEV)) {
            LOG.fine("GET dev, skipping header checking");
            return;
        }

        if (ctx.getMethod().equals(REST_METHOD_NAME_OPTIONS)) {
            LOG.fine(REST_METHOD_NAME_OPTIONS + ", skipping header checking");
            return;
        }

        final List<String> authHeaderList = ctx.getHeaders().get(HEADER_NAME_AUTH);
        if (authHeaderList == null) {
            LOG.fine("Authorization header '" + HEADER_NAME_AUTH + "' missing, denying request");
            ctx.abortWith(Response.status(401).build());
            return;
        }

        final String authValue = authHeaderList.get(0);
        if (authValue == null || authValue.trim().equals("")) {
            LOG.fine("Authorization header '" + HEADER_NAME_AUTH + "' empty, denying request");
            ctx.abortWith(Response.status(401).build());
            return;
        }

        LOG.fine("auth token found: '" + authValue + "'");
        final AuthResponse authResponse = service.authorize(authValue);
        if (!authResponse.isSuccess()) {
            LOG.fine("Authentication failed, " + authResponse + ", denying request");
            ctx.abortWith(Response.status(401).build());
        } else {
            final String userName = authResponse.getUser().getUserName();
            ctx.setProperty(HEADER_NAME_LOGGEDINUSER, userName);
            ctx.getHeaders().add(HEADER_NAME_LOGGEDINUSER, userName);
            LOG.fine("authentication successful, setting user into rctx: " + userName);
        }
    }
}
