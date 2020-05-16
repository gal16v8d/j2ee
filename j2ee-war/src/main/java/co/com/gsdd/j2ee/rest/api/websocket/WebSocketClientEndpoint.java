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

import co.com.gsdd.j2ee.db.jpa.Person;
import co.com.gsdd.j2ee.ejb.PersonEJB;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ClientEndpoint
public class WebSocketClientEndpoint {

	@EJB
	private PersonEJB personEJB;

	public void initConnection() {
		try {
			WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
			webSocketContainer.connectToServer(this, UriBuilder.fromPath("ws://localhost:8080/j2ee/websocket").build());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig conf) throws IOException {
		log.debug("client session with hash -> {}", session.hashCode());
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
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	@OnClose
	public void onClose(Session session) {
		log.debug("removed client session with hash -> {}", session.hashCode());
		try {
			session.close();
		} catch (Exception e) {
			log.error("Error closing client connection.", e);
		}
	}

	@OnError
	public void onError(Throwable error) {
		log.error("Error on client side. {}", error.getMessage(), error);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("Client received message -> {}", message);
	}

}
