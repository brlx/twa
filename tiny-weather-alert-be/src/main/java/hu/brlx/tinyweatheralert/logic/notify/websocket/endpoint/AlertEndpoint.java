package hu.brlx.tinyweatheralert.logic.notify.websocket.endpoint;

import hu.brlx.tinyweatheralert.logic.auth.AuthResponse;
import hu.brlx.tinyweatheralert.logic.auth.AuthService;
import hu.brlx.tinyweatheralert.logic.notify.websocket.WebsocketEndpointManager;
import hu.brlx.tinyweatheralert.logic.notify.websocket.WsAuthResponse;
import hu.brlx.tinyweatheralert.logic.notify.websocket.message.*;
import hu.brlx.tinyweatheralert.logic.notify.websocket.message.Error;
import hu.brlx.tinyweatheralert.persistence.entity.User;

import javax.inject.Inject;
import javax.websocket.Session;
import java.util.logging.Logger;

public class AlertEndpoint {

    private static final Logger LOG = Logger.getLogger(AlertEndpoint.class.getName());

    private Session wsSession;

    private boolean authenticated;

    private User user;

    @Inject
    private AuthService service;

    @Inject
    private WebsocketEndpointManager endpointManager;

    protected void setSession(Session session) {
        wsSession = session;
    }

    /**
     * Sends a text message to this endpoint
     * @param message The text message
     */
    public void sendMessage(String message) {
        wsSession.getAsyncRemote().sendText(message);
    }

    /**
     * Handles the message and returns the reply
     * @param message The incoming message
     * @return The reply to the message
     */
    public WsMessage handleMessage(WsMessage message) {
        switch (message.getMessageType()) {
            case "Authenticate":
                final Authenticate authMessage = (Authenticate) message;
                LOG.fine("authenticating " + authMessage);
                final AuthResponse wsAuthResponse = service.authorize(authMessage.getUserName(), authMessage.getToken());
                if (!wsAuthResponse.isSuccess()) {
                    return new AuthenticationFailed();
                }
                this.user = wsAuthResponse.getUser();
                this.authenticated = true;
                endpointManager.authenticate(this);
                return new AuthenticationSuccessful();
            default:
                final String errorMsg = "Unhandled message type: " + message.getMessageType();
                LOG.warning(errorMsg);
                return new Error(errorMsg);
        }
    }

    public String getUsername() {
        return user != null ? user.getUserName() : null;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public String toString() {
        return "AlertEndpoint [" +
                "wsSession=" + (wsSession != null ? wsSession.getId() : "null") +
                ", authenticated=" + authenticated +
                ", user=" + (user != null ? user.getUserName() : "null") +
                ']';
    }
}
