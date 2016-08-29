package hu.brlx.tinyweatheralert.logic.notify.websocket.message;

@SuppressWarnings("unused")
public class AlertMessage extends WsMessage {

    private String city;

    private String threshold;

    private String currentTemperature;

    public AlertMessage(String city, String threshold, String currentTemperature) {
        this.messageType = "Alert";
        this.city = city;
        this.threshold = threshold;
        this.currentTemperature = currentTemperature;
    }

    @Override
    public String toString() {
        return "AlertMessage [" +
                "messageType=" + messageType +
                ", city='" + city + '\'' +
                ", threshold='" + threshold + '\'' +
                ", currentTemperature='" + currentTemperature + '\'' +
                ']';
    }
}
