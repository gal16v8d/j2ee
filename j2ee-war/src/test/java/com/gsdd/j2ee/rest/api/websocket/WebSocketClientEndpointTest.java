package com.gsdd.j2ee.rest.api.websocket;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import javax.websocket.EndpointConfig;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gsdd.j2ee.db.jpa.Person;
import com.gsdd.j2ee.ejb.PersonEJB;

@ExtendWith(MockitoExtension.class)
class WebSocketClientEndpointTest {

    @Spy
    @InjectMocks
    private WebSocketClientEndpoint webSocketClientEndpoint;
    @Mock
    private PersonEJB personEJB;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void onOpenErrorTest(@Mock Session session, @Mock EndpointConfig conf, @Mock Basic basic) throws IOException {
        Mockito.doReturn(basic).when(session).getBasicRemote();
        Mockito.doNothing().when(basic).sendText(Mockito.anyString());
        Mockito.doThrow(new RuntimeException()).when(personEJB).findAll();
        webSocketClientEndpoint.onOpen(session, conf);
        Mockito.verify(basic).sendText(Mockito.anyString());
    }

    @ParameterizedTest
    @MethodSource("noPersonInDb")
    void onOpenNoResultTest(List<Person> person, @Mock Session session, @Mock EndpointConfig conf, @Mock Basic basic)
            throws IOException {
        Mockito.doReturn(basic).when(session).getBasicRemote();
        Mockito.doNothing().when(basic).sendText(Mockito.anyString());
        Mockito.doReturn(person).when(personEJB).findAll();
        webSocketClientEndpoint.onOpen(session, conf);
        Mockito.verify(basic).sendText(Mockito.anyString());
    }

    private static Stream<Arguments> noPersonInDb() {
        return Stream.of(null, Arguments.of(Collections.emptyList()));
    }

    @Test
    void onOpenTest(@Mock Session session, @Mock EndpointConfig conf, @Mock Basic basic) throws IOException {
        Mockito.doReturn(basic).when(session).getBasicRemote();
        Mockito.doNothing().when(basic).sendText(Mockito.anyString());
        Mockito.doReturn(arrangePersonList()).when(personEJB).findAll();
        webSocketClientEndpoint.onOpen(session, conf);
        Mockito.verify(basic, Mockito.times(3)).sendText(Mockito.anyString());
    }

    private List<Person> arrangePersonList() {
        return Arrays.asList(new Person());
    }

    @Test
    void onCloseErrorTest(@Mock Session session) throws IOException {
        Mockito.doThrow(new IOException()).when(session).close();
        webSocketClientEndpoint.onClose(session);
        Mockito.verify(session).close();
    }

    @Test
    void onCloseTest(@Mock Session session) throws IOException {
        Mockito.doNothing().when(session).close();
        webSocketClientEndpoint.onClose(session);
        Mockito.verify(session).close();
    }
}
