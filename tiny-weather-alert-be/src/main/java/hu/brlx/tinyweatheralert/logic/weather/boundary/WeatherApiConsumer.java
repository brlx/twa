package hu.brlx.tinyweatheralert.logic.weather.boundary;

import com.google.gson.Gson;
import hu.brlx.tinyweatheralert.logic.weather.boundary.model.CityWeatherModel;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherApiConsumer {

    private static final Logger LOG = Logger.getLogger(WeatherApiConsumer.class.getName());

    private static final String API_URL_ROOT = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_PARAM_CITYID = "id";
    private static final String API_PARAM_APIKEY = "APPID";
    private static final String API_APIKEY = "3ae4190cb7da98b7fd127d3eb4380a49";
    private static final String API_PARAM_UNITS = "units";
    private static final String API_UNITS = "metric";

    private Gson gson;

    public WeatherApiConsumer() {
        gson = new Gson();
    }

    public WeatherApiResponse requestWeather(Long cityID) {
        Client client = ClientBuilder.newClient();
        String rawResponse = null;
        try {
            final WebTarget webTarget = client
                    .target(API_URL_ROOT)
                    .queryParam(API_PARAM_APIKEY, API_APIKEY)
                    .queryParam(API_PARAM_UNITS, API_UNITS)
                    .queryParam(API_PARAM_CITYID, cityID);
            rawResponse = webTarget.request(MediaType.APPLICATION_JSON_TYPE)
                    .get(String.class);
            final CityWeatherModel cityWeatherModel = gson.fromJson(rawResponse, CityWeatherModel.class);
            return WeatherApiResponse.success(cityWeatherModel);
        } catch (com.google.gson.JsonSyntaxException jse) {
          LOG.log(Level.WARNING, "Could not extract result from json response \"" + rawResponse +  "\"", jse);
            return WeatherApiResponse.failed("Could not extract result from json response");
        } catch (Exception e) {
            final String errorMsg = "ERROR when fetching the data for city '" + cityID + "' from the weaetherAPI";
            LOG.log(Level.WARNING, errorMsg , e);
            return WeatherApiResponse.failed(errorMsg);
        }
    }

}
