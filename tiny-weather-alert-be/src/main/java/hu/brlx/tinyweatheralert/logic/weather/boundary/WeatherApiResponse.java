package hu.brlx.tinyweatheralert.logic.weather.boundary;

import hu.brlx.tinyweatheralert.logic.Response;
import hu.brlx.tinyweatheralert.logic.weather.boundary.model.CityWeatherModel;

public class WeatherApiResponse extends Response {

    private CityWeatherModel cityWeather;

    protected WeatherApiResponse() {}

    public static WeatherApiResponse success(CityWeatherModel model) {
        final WeatherApiResponse resp = new WeatherApiResponse();
        resp.success = true;
        resp.cityWeather = model;
        return resp;
    }

    public static WeatherApiResponse failed(String message) {
        final WeatherApiResponse resp = new WeatherApiResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }

    public CityWeatherModel getCityWeather() {
        return cityWeather;
    }

    @Override
    public String toString() {
        return "WeatherApiResponse [" +
                "success=" + success +
                "message=" + message +
                "cityWeather=" + cityWeather +
                ']';
    }
}
