package hu.brlx.tinyweatheralert.api.alerts;

import hu.brlx.tinyweatheralert.persistence.entity.City;

@SuppressWarnings("unused")
public class CityModel {

    private Long id;

    private String name;

    private String country;

    public CityModel(City city) {
        this.id = city.getId();
        this.name = city.getName();
        this.country = city.getCountry();
    }

}
