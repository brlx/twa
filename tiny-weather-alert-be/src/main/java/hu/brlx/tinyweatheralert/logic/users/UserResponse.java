package hu.brlx.tinyweatheralert.logic.users;

import hu.brlx.tinyweatheralert.logic.Response;

public class UserResponse extends Response {

    // TODO
    private String cookie;

    protected UserResponse() {
        super();
    };

    public static UserResponse success() {
        final UserResponse resp = new UserResponse();
        resp.success = true;
        return resp;
    }

    public static UserResponse success(String cookie) {
        final UserResponse resp = new UserResponse();
        resp.success = true;
        resp.cookie = cookie;
        return resp;
    }

    public static UserResponse success(String message, String cookie) {
        final UserResponse resp = new UserResponse();
        resp.success = true;
        resp.message = message;
        resp.cookie = cookie;
        return resp;
    }


    public static UserResponse failed(String message) {
        final UserResponse resp = new UserResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }

}
