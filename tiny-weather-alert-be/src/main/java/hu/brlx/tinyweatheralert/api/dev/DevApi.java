package hu.brlx.tinyweatheralert.api.dev;

import com.google.gson.Gson;
import hu.brlx.tinyweatheralert.logic.notify.websocket.WebsocketEndpointManager;
import hu.brlx.tinyweatheralert.logic.notify.websocket.message.AlertMessage;
import hu.brlx.tinyweatheralert.logic.weather.controller.WeatherService;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;

@Path("dev")
@Stateless
public class DevApi {

    private static final Logger LOG = Logger.getLogger(DevApi.class.getName());

    private Gson gson;

    @Inject
    private WeatherService weatherService;

    @Inject
    private WebsocketEndpointManager websocketManager;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    @GET
    @Path("refreshWeather")
    @Produces(MediaType.APPLICATION_JSON)
    public String refreshWeather(@QueryParam("cityid") String rawCityId) {
        LOG.fine("refreshWeather, cityid=" + rawCityId);

        weatherService.refreshWeatherFromProvider();

        return "{\"success\": true}";
    }

    @GET
    @Path("sendnotification")
    @Produces(MediaType.APPLICATION_JSON)
    public String sendNotification(@QueryParam("username") String username,
                                   @QueryParam("message") String message) {
        LOG.fine("sendNotification, username=" + username + ", message=" + message);

        final AlertMessage alertMsg = new AlertMessage("BudapestDEV", "20", "23");

        final boolean success = websocketManager.sendMessage("neo", alertMsg);

        return "{\"success\": " + success + "}";
    }
}
