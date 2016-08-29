package hu.brlx.tinyweatheralert.api;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Provider
public class HeaderFilter implements ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(HeaderFilter.class.getName());

    private static final String CORS_HEDAER_NAME = "Access-Control-Allow-Origin";
    private static final String ALLOWHEADERS_HEADER_NAME = "Access-Control-Allow-Headers";

    @Override
    public void filter(ContainerRequestContext requestC, ContainerResponseContext responseC) throws IOException {
        responseC.getHeaders().add(CORS_HEDAER_NAME, "*");
        List<Object> allowHeadersList = responseC.getHeaders().get(ALLOWHEADERS_HEADER_NAME);
        if (allowHeadersList == null) {
            responseC.getHeaders().put(ALLOWHEADERS_HEADER_NAME, new ArrayList<>());
        }
        allowHeadersList = responseC.getHeaders().get(ALLOWHEADERS_HEADER_NAME);
        allowHeadersList.add(AuthFilter.HEADER_NAME_AUTH);
    }
}
