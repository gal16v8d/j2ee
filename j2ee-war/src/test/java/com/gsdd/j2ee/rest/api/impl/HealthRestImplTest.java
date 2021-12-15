package com.gsdd.j2ee.rest.api.impl;

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
class HealthRestImplTest {

    @Spy
    private HealthRestImpl healthRest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void healthCheck() {
        Response response = healthRest.healthCheck();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }

}
