package hu.brlx.tinyweatheralert.logic.notify.websocket;

import hu.brlx.tinyweatheralert.logic.Response;
import hu.brlx.tinyweatheralert.persistence.entity.User;

public class WsAuthResponse extends Response {

    private User user;

    public static WsAuthResponse success(User user) {
        final WsAuthResponse resp = new WsAuthResponse();
        resp.success = true;
        resp.user = user;
        return resp;
    }

    public static WsAuthResponse failed(String message) {
        final WsAuthResponse resp = new WsAuthResponse();
        resp.success = false;
        resp.message = message;
        resp.user = null;
        return resp;
    }

    public User getUser() {
        return user;
    }
}
