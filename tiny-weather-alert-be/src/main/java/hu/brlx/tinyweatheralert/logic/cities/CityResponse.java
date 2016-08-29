package hu.brlx.tinyweatheralert.logic.cities;

import hu.brlx.tinyweatheralert.logic.Response;
import hu.brlx.tinyweatheralert.persistence.entity.City;

import java.util.List;

public class CityResponse extends Response {

    private List<City> cities;

    public static CityResponse success(List<City> cities) {
        final CityResponse resp = new CityResponse();
        resp.success = true;
        resp.cities = cities;
        return resp;
    }

    public static CityResponse failed(String message) {
        final CityResponse resp = new CityResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }

}
