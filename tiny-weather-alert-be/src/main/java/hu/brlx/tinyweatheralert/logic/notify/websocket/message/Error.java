package hu.brlx.tinyweatheralert.logic.notify.websocket.message;

@SuppressWarnings("unused") // only gson will access the fields
public class Error extends WsMessage {

    private String errorMessage;

    public Error(String errorMessage) {
        this.messageType = "Error";
        this.errorMessage = errorMessage;
    }
}
