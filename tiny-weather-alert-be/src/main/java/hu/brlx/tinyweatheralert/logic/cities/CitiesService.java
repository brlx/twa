package hu.brlx.tinyweatheralert.logic.cities;

import hu.brlx.tinyweatheralert.persistence.Dao;
import hu.brlx.tinyweatheralert.persistence.entity.City;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Transactional
public class CitiesService {

    @Inject
    private Dao dao;

    public CityResponse searchCitiesByName(String name) {

        final List<City> cities = dao.searchCityByName("%" + name + "%");

        return CityResponse.success(cities);
    }

}
