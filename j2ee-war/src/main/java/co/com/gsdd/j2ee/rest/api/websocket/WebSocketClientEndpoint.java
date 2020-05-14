package co.com.gsdd.j2ee.rest.api.websocket;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.gsdd.j2ee.db.jpa.Person;
import co.com.gsdd.j2ee.ejb.PersonEJB;

@ClientEndpoint
public class WebSocketClientEndpoint {

    @EJB
    private PersonEJB personEJB;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientEndpoint.class);

    public void initConnection() {
        try {
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            webSocketContainer.connectToServer(this, UriBuilder.fromPath("ws://localhost:8080/j2ee/websocket").build());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) throws IOException {
        LOGGER.debug("client session with hash -> {}", session.hashCode());
        try {
            session.getBasicRemote().sendText("Client -> " + session.hashCode() + " reporting to server.");
            List<Person> lp = personEJB.findAll();
            if (lp != null && !lp.isEmpty()) {
                session.getBasicRemote().sendText("Client -> " + session.hashCode() + " hey! server I found persons!!");
                for (Person p : lp) {
                    session.getBasicRemote().sendText("Client -> " + session.hashCode() + " I'll send this person -> "
                            + p.getName() + " " + p.getLastName());
                }
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @OnClose
    public void onClose(Session session) {
        LOGGER.debug("removed client session with hash -> {}", session.hashCode());
        try {
            session.close();
        } catch (IOException e) {
            LOGGER.error("Error closing client connection.", e);
        }
    }

    @OnError
    public void onError(Throwable error) {
        LOGGER.error("Error on client side. " + error.getMessage(), error);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.info("Client received message -> {}", message);
    }

}
