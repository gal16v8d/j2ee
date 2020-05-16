package co.com.gsdd.j2ee.rest.api.impl;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import co.com.gsdd.j2ee.rest.api.WSClientRest;
import co.com.gsdd.j2ee.rest.api.websocket.WebSocketClientEndpoint;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WSClientRestImpl implements WSClientRest {

    @Inject
    private WebSocketClientEndpoint webSocketClient;

    @Override
    public Response checkWebSocketClient() {
        Response response;
        try {
            webSocketClient.initConnection();
            response = Response.ok().build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = Response.serverError().entity(e.getMessage()).build();
        }
        return response;
    }
}
