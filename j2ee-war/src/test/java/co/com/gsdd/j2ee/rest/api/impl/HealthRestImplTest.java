package co.com.gsdd.j2ee.rest.api.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HealthRestImplTest {

    @Spy
    private HealthRestImpl healthRest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void healthCheck() {
        Response response = healthRest.healthCheck();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

}
