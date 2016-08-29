package hu.brlx.tinyweatheralert.logic.weather.boundary.model;

public class CityWeatherModel {

    private Long id;

    private String name;

    private MainWeatherModel main;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MainWeatherModel getMain() {
        return main;
    }

    @Override
    public String toString() {
        return "CityWeatherModel [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", main=" + main +
                '}';
    }
}
