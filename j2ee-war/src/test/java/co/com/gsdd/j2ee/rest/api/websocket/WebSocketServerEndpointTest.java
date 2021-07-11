package co.com.gsdd.j2ee.rest.api.websocket;

import java.io.IOException;

import javax.websocket.EndpointConfig;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebSocketServerEndpointTest {

    @Spy
    private WebSocketServerEndpoint webSocketServerEndpoint;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onOpenTest(@Mock Session session, @Mock EndpointConfig conf, @Mock Basic basic) throws IOException {
        Mockito.when(session.getBasicRemote()).thenReturn(basic);
        Mockito.doNothing().when(basic).sendText(Mockito.anyString());
        webSocketServerEndpoint.onOpen(session, conf);
        Mockito.verify(basic).sendText(Mockito.anyString());
    }

    @Test
    void onMessageTest(@Mock Session session, @Mock EndpointConfig conf, @Mock Basic basic) throws IOException {
        Mockito.when(session.getBasicRemote()).thenReturn(basic);
        Mockito.doNothing().when(basic).sendText(Mockito.anyString());
        webSocketServerEndpoint.onOpen(session, conf);
        webSocketServerEndpoint.onMessage("Test", session);
        Mockito.verify(basic, Mockito.times(2)).sendText(Mockito.anyString());
    }

    @Test
    void onMessageNoSessionTest(@Mock Session session) throws IOException {
        webSocketServerEndpoint.onMessage("Test", session);
        Mockito.verify(session, Mockito.never()).getBasicRemote();
    }
}
