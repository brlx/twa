package hu.brlx.tinyweatheralert.api.alerts;

import hu.brlx.tinyweatheralert.logic.alerts.AlertsResponse;
import hu.brlx.tinyweatheralert.persistence.entity.Alert;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlertListResponse extends AlertsResponse {

    private List<AlertModel> alerts;

    protected AlertListResponse(){}

    public static AlertListResponse success(List<Alert> alerts, Map<Long, Float> tempMap) {
        final AlertListResponse resp = new AlertListResponse();
        resp.success = true;
        resp.alerts = alerts.stream()
                            .map((alert) -> new AlertModel(alert,
                                                           tempMap.get(alert.getCity().getId())))
                .collect(Collectors.toList());
        return resp;
    }

    public static AlertListResponse failed(String message) {
        final AlertListResponse resp = new AlertListResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }
}
