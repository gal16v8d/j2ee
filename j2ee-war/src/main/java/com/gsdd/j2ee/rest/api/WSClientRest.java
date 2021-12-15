package com.gsdd.j2ee.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.gsdd.j2ee.rest.api.util.RestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("WebSockets")
@Path("websocketTest")
public interface WSClientRest {

    @ApiOperation(value = "Launch web socket action query some person on db")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Ok|Success"),
            @ApiResponse(code = 500, message = RestUtil.SWAGGER_DOC_UNEXPECTED_ERROR) })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response checkWebSocketClient();

}
