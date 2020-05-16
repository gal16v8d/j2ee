package co.com.gsdd.j2ee.rest.api;

import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Health")
@Path("/health")
public interface HealthRest {

    @HEAD
    @ApiOperation(value = "Allows to check is app is Up or Down")
    Response healthCheck();
}
