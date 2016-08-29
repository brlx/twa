package hu.brlx.tinyweatheralert.logic.auth;

import hu.brlx.tinyweatheralert.logic.Response;

public class LogoutResponse extends Response {

    public static LogoutResponse success() {
        final LogoutResponse resp = new LogoutResponse();
        resp.success = true;
        return resp;
    }

    public static LogoutResponse failed(String message) {
        final LogoutResponse resp = new LogoutResponse();
        resp.success = true;
        resp.message = message;
        return resp;
    }
}
