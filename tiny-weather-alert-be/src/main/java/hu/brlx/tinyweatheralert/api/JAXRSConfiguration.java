package hu.brlx.tinyweatheralert.api;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;

@ApplicationPath("api")
public class JAXRSConfiguration extends Application {

    public static String extractUserNameFromHeader(HttpHeaders headers) {
        final List<String> loggedInHeaderList = headers.getRequestHeader(AuthFilter.HEADER_NAME_LOGGEDINUSER);
        if (loggedInHeaderList == null || loggedInHeaderList.size() == 0) {
            return null;
        }
        return loggedInHeaderList.get(0);
    }

}
