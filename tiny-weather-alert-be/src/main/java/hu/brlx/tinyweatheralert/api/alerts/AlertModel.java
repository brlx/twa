package hu.brlx.tinyweatheralert.api.alerts;

import hu.brlx.tinyweatheralert.logic.Utils;
import hu.brlx.tinyweatheralert.persistence.entity.Alert;

@SuppressWarnings("unused")
public class AlertModel {

    private Long id;

    private CityModel city;

    private Integer threshold;

    private Float currentTemp;

    private Long lastTriggered;

    private boolean todayTriggered;

    private Long lastNotified;

    public AlertModel(Alert alert, Float currentTemp) {
        this.id = alert.getId();
        this.city = new CityModel(alert.getCity());
        this.threshold = alert.getThreshold();
        this.lastTriggered = alert.getLastTriggered().getTime();
        this.lastNotified = alert.getLastNotified().getTime();
        this.todayTriggered = Utils.todayStart().getTime() < this.lastTriggered;
        this.currentTemp = currentTemp;
    }
}
