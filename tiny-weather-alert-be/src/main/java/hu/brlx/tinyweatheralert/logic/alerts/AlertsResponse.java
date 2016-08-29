package hu.brlx.tinyweatheralert.logic.alerts;

import hu.brlx.tinyweatheralert.logic.Response;

public class AlertsResponse extends Response {

    protected AlertsResponse() {}

    public static AlertsResponse success() {
        final AlertsResponse resp = new AlertsResponse();
        resp.success = true;
        return resp;
    }

    public static AlertsResponse success(String message) {
        final AlertsResponse resp = new AlertsResponse();
        resp.success = true;
        resp.message = message;
        return resp;
    }

    public static AlertsResponse failed(String message) {
        final AlertsResponse resp = new AlertsResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }

}
