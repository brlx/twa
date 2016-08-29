package hu.brlx.tinyweatheralert.logic.auth;

import hu.brlx.tinyweatheralert.logic.Response;
import hu.brlx.tinyweatheralert.persistence.entity.User;

/**
 * Response for the client when it tries to access a resource with a previously established cookie.
 */
public class AuthResponse extends Response {

    private User user;

    public static AuthResponse success(User user) {
        final AuthResponse resp = new AuthResponse();
        resp.success = true;
        resp.user = user;
        return resp;
    }

    public static AuthResponse failed(String message) {
        final AuthResponse resp = new AuthResponse();
        resp.success = false;
        resp.message = message;
        return resp;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "AuthResponse [" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", user=" + (user != null ? user.getUserName() : "null") +
                "]";
    }
}
