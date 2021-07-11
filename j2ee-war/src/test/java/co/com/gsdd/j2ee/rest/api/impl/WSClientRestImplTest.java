package co.com.gsdd.j2ee.rest.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.gsdd.j2ee.rest.api.websocket.WebSocketClientEndpoint;

@ExtendWith(MockitoExtension.class)
class WSClientRestImplTest {

    @Spy
    @InjectMocks
    private WSClientRestImpl wsClientRestImpl;
    @Mock
    private WebSocketClientEndpoint webSocketClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkWebSocketClientTest() {
        Mockito.doNothing().when(webSocketClient).initConnection();
        Response response = wsClientRestImpl.checkWebSocketClient();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    void checkWebSocketClientExcTest() {
        Mockito.doThrow(new RuntimeException()).when(webSocketClient).initConnection();
        Response response = wsClientRestImpl.checkWebSocketClient();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
}
