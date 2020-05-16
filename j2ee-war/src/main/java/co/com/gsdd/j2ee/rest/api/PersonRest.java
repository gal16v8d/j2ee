package co.com.gsdd.j2ee.rest.api;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.com.gsdd.j2ee.db.jpa.Person;
import co.com.gsdd.j2ee.rest.api.request.PersonRequest;
import co.com.gsdd.j2ee.rest.api.util.RestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("Persons")
@Path("persons")
public interface PersonRest {

    @ApiOperation(value = "Finds Person using a proxy client from jeeservice", response = co.com.gsdd.jeeservice.model.Person.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Person from proxy", response = co.com.gsdd.jeeservice.model.Person.class),
            @ApiResponse(code = 500, message = RestUtil.SWAGGER_DOC_UNEXPECTED_ERROR) })
    @GET
    @Path("proxy")
    @Produces(MediaType.APPLICATION_JSON)
    Response consumeRestService();

    @ApiOperation(value = "Retrieve all Persons from database", response = Person.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If exists any Person on database", response = Person.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "If no Person on database"),
            @ApiResponse(code = 500, message = RestUtil.SWAGGER_DOC_UNEXPECTED_ERROR) })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getAllPersons();

    @ApiOperation(value = "Retrieve a single Person from database", response = Person.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If exists any matching Person on database", response = Person.class),
            @ApiResponse(code = 404, message = "If no matching Person on database"),
            @ApiResponse(code = 500, message = RestUtil.SWAGGER_DOC_UNEXPECTED_ERROR) })
    @GET
    @Path(RestUtil.PATH_PERSON_ID)
    @Produces(MediaType.APPLICATION_JSON)
    Response getPerson(@ApiParam(required = true) @PathParam(RestUtil.PERSON_ID) String personId);

    @ApiOperation(value = "Allows to store a Person in database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "If the Person can be stored on database", response = Person.class),
            @ApiResponse(code = 500, message = RestUtil.SWAGGER_DOC_UNEXPECTED_ERROR) })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response savePerson(@Valid PersonRequest request);

    @ApiOperation(value = "Allows to update a Person in database", response = Person.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "If the Person can be updated on database", response = Person.class),
            @ApiResponse(code = 304, message = "If the Person can not be updated on database"),
            @ApiResponse(code = 500, message = RestUtil.SWAGGER_DOC_UNEXPECTED_ERROR) })
    @PUT
    @Path(RestUtil.PATH_PERSON_ID)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateUser(@ApiParam(required = true) @PathParam(RestUtil.PERSON_ID) String personId,
            @Valid PersonRequest request);

    @ApiOperation(value = "Allows to delete a Person in database")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "If the Person can be deleted on database"),
            @ApiResponse(code = 304, message = "If the Person can not be deleted on database"),
            @ApiResponse(code = 500, message = RestUtil.SWAGGER_DOC_UNEXPECTED_ERROR) })
    @DELETE
    @Path(RestUtil.PATH_PERSON_ID)
    @Produces(MediaType.APPLICATION_JSON)
    Response deletePerson(@ApiParam(required = true) @PathParam(RestUtil.PERSON_ID) String personId);

}
