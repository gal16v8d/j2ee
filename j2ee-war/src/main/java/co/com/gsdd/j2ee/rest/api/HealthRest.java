package co.com.gsdd.j2ee.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;

@Api("Health")
@Path("/health")
public class HealthRest {

    @GET
    public Response healthCheck() {
        return Response.ok().build();
    }
}
