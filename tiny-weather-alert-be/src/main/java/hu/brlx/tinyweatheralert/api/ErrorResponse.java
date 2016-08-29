package hu.brlx.tinyweatheralert.api;

import hu.brlx.tinyweatheralert.logic.Response;

public class ErrorResponse extends Response {

    public static ErrorResponse failed(String message) {
        final ErrorResponse resp = new ErrorResponse();
        resp.message = message;
        return resp;
    }
}
