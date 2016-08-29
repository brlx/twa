package hu.brlx.tinyweatheralert.logic.notify.websocket.message;

public class AuthenticationSuccessful extends WsMessage {

    public AuthenticationSuccessful() {
        this.messageType = this.getClass().getSimpleName();
    }

}
