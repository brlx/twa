package hu.brlx.tinyweatheralert.logic.notify.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import hu.brlx.tinyweatheralert.logic.notify.websocket.endpoint.AlertEndpoint;
import hu.brlx.tinyweatheralert.logic.notify.websocket.endpoint.EndpointFactory;
import hu.brlx.tinyweatheralert.logic.notify.websocket.message.*;
import hu.brlx.tinyweatheralert.logic.notify.websocket.message.Error;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@ServerEndpoint(value = "/realtimealerts")
public class WebsocketEndpointManager {

    private static final Logger LOG = Logger.getLogger(WebsocketEndpointManager.class.getName());

    final private Map<String, AlertEndpoint> endpointsBySession = new HashMap<>();
    final private Map<String, AlertEndpoint> endpointsByUser = new HashMap<>();

    @Inject
    private EndpointFactory endpointFactory;

    private Gson gson;

    @PostConstruct
    public void init() {
        gson = new Gson();
    }

    /**
     * Sends the message to the specified user.
     * @param userName
     * @param message
     * @return Returns true if the message could be delivered
     */
    public boolean sendMessage(String userName, WsMessage message) {
        LOG.fine("sendMessage, userName=" + userName + ", message=" + message);
        final AlertEndpoint endpoint = endpointsByUser.get(userName);
        if (endpoint == null) {
            LOG.fine("  endpoint not found");
            return false;
        }
        endpoint.sendMessage(gson.toJson(message));
        return true;
    }

    public void authenticate(AlertEndpoint endpoint) {
        LOG.fine("authenticate: endpoint=" + endpoint);
        endpointsByUser.put(endpoint.getUsername(), endpoint);
    }

    @OnMessage
    public String onMessage(String rawMessage, Session session) {
        LOG.fine("onMessage, rawMessage=" + rawMessage);
        WsMessage responseMessage;
        final AlertEndpoint endpoint = endpointsBySession.get(session.getId());
        if (endpoint == null) {
            LOG.warning("Incoming message from unknown session: " + session.getId() + ", " + session);
            return gson.toJson(new Error("Sender not found"));
        }
        WsMessage rawWsMessage;
        try {
            rawWsMessage = gson.fromJson(rawMessage, WsMessage.class);
        } catch (JsonSyntaxException jse) {
            LOG.log(Level.WARNING, "Error when parsing message from WS: '" + rawMessage + "'", jse);
            return gson.toJson(new Error("Invalid JSON message"));
        }
        try {
            LOG.fine("  rawMessage type: " + rawWsMessage.getMessageType());
            final WsMessage message =
                    (WsMessage) gson.fromJson(rawMessage, Class.forName(WsMessage.class.getPackage().getName() + "." + rawWsMessage.getMessageType()));
            responseMessage = endpoint.handleMessage(message);
        } catch (ClassNotFoundException e) {
            responseMessage = new Error("Unhandled message type: " + rawWsMessage.getMessageType());
        }
        return gson.toJson(responseMessage);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig epConfig) {
        LOG.fine("onOpen");
        session.getUserProperties().entrySet().forEach(entry -> {
            LOG.fine("  " + entry.getKey() + " - " + entry.getValue());
        });

        final AlertEndpoint endpoint = endpointFactory.create(session);
        endpointsBySession.put(session.getId(), endpoint);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOG.fine("onClose, " + reason.getCloseCode() + " - " + reason.getReasonPhrase());
        final AlertEndpoint removedEndpoint = endpointsBySession.remove(session.getId());
        LOG.fine("  endpoint removed: " + removedEndpoint);
        if (removedEndpoint.isAuthenticated()) {
            final AlertEndpoint removedAuthEndpoint = endpointsByUser.remove(removedEndpoint.getUsername());
            LOG.fine("  endpoint also removed from authenticated ones: " + removedAuthEndpoint);
        }
    }

    @OnError
    public void onError(Session session, Throwable t) {
        final AlertEndpoint endpoint = endpointsBySession.get(session.getId());
        final String errorMsg = t.getClass().getName() + ": " + t.getMessage();
        if (endpoint != null) {
            LOG.log(Level.FINE, "Websocket error with endpoint " + endpoint + ", " + errorMsg);
        } else {
            LOG.log(Level.FINE, "Websocket error with session" + session.getId() + ", " + errorMsg);
        }
    }

}
