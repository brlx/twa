package hu.brlx.tinyweatheralert.logic.weather.boundary.model;

public class MainWeatherModel {

    private Float temp;

    private Float pressure;

    private Float humidity;

    public Float getTemp() {
        return temp;
    }

    public Float getPressure() {
        return pressure;
    }

    public Float getHumidity() {
        return humidity;
    }

    @Override
    public String toString() {
        return "MainWeatherModel [" +
                "temp=" + temp +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ']';
    }
}
