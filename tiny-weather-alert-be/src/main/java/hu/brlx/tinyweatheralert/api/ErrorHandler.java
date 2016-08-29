package hu.brlx.tinyweatheralert.api;

import com.google.gson.Gson;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class ErrorHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(ErrorResponse.class.getName());

    private Gson gson;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    @Override
    public Response toResponse(Exception exception) {
        final String errorMsg = "Internal error: " + exception.getClass().getSimpleName() + " - " + exception.getMessage();
        LOG.log(Level.WARNING, "Error during handling a request", exception);
        return Response.status(500).entity(gson.toJson(ErrorResponse.failed(errorMsg))).build();
    }
}
