package hu.brlx.tinyweatheralert.logic.notify.websocket.endpoint;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.websocket.Session;

@Dependent
public class EndpointFactory {

    @Inject
    private Instance<AlertEndpoint> endpointProvider;

    public AlertEndpoint create(Session session) {
        final AlertEndpoint alertEndpoint = endpointProvider.get();
        alertEndpoint.setSession(session);
        return alertEndpoint;
    }
}
