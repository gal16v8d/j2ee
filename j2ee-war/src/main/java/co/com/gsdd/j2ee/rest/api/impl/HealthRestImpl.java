package co.com.gsdd.j2ee.rest.api.impl;

import javax.ws.rs.core.Response;

import co.com.gsdd.j2ee.rest.api.HealthRest;

public class HealthRestImpl implements HealthRest {

    @Override
    public Response healthCheck() {
        return Response.ok().build();
    }
}
