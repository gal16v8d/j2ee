package co.com.gsdd.j2ee.rest.api.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/websocket")
public class WebSocketServerEndpoint {

    private static final List<Session> SESSIONS = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServerEndpoint.class);

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) throws IOException {
        LOGGER.debug("adding session with hash -> {}", session.hashCode());
        SESSIONS.add(session);
        for (Session sess : SESSIONS) {
            sess.getBasicRemote().sendText("Server says Hi to client -> " + sess.hashCode());
        }
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.debug("removed session with hash -> {}", session.hashCode());
        SESSIONS.remove(session);
    }

    @OnError
    public void onError(Throwable error) {
        LOGGER.error("Error on server side. " + error.getMessage(), error);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            LOGGER.info("Server received message -> {} from session -> {}", message, session.hashCode());
            for (Session sess : SESSIONS) {
                sess.getBasicRemote().sendText("Server is ready for new messages from -> " + sess.hashCode());
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

}
