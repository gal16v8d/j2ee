package co.com.gsdd.j2ee.rest.api;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.com.gsdd.j2ee.rest.api.websocket.WebSocketClientEndpoint;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api("WebSockets")
@Path("websocketTest")
public class WSClientRest {

    @Inject
    private WebSocketClientEndpoint webSocketClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response testWebSocketClient() {
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
