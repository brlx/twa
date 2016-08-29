package hu.brlx.tinyweatheralert.logic;

public abstract class Response {

    protected boolean success;

    protected String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Response [" +
                "success=" + success +
                ", message='" + message + '\'' +
                ']';
    }
}
