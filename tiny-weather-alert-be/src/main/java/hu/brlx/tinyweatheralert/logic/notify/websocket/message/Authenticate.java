package hu.brlx.tinyweatheralert.logic.notify.websocket.message;

@SuppressWarnings("unused")
public class Authenticate extends WsMessage {

    private String userName;

    private String token;

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "Authenticate [" +
                "userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ']';
    }
}
