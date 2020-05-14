package co.com.gsdd.j2ee.rest.api;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.inject.Inject;
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
import javax.ws.rs.core.Response.Status;

import co.com.gsdd.j2ee.db.jpa.Person;
import co.com.gsdd.j2ee.ejb.PersonEJB;
import co.com.gsdd.j2ee.ejb.PersonProxy;
import co.com.gsdd.j2ee.rest.api.request.PersonRequest;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api("Persons")
@Path("persons")
public class PersonRest {

    private static final String PATH_PERSON_ID = "/{personId}";
    private static final String PERSON_ID = "personId";

    @EJB
    private PersonEJB personEJB;
    @Inject
    private PersonProxy personProxy;

    @GET
    @Path("proxy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consumeRestService() {
        Response response;
        try {
            response = Response.ok(personProxy.getPersonFromProxy()).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = Response.serverError().entity(e.getMessage()).build();
        }
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        Response response;
        try {
            List<Person> lp = personEJB.findAll();
            if (lp != null && !lp.isEmpty()) {
                response = Response.ok(lp).build();
            } else {
                response = Response.status(Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = Response.serverError().entity(e.getMessage()).build();
        }
        return response;
    }

    @GET
    @Path(PATH_PERSON_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(@PathParam(PERSON_ID) String personId) {
        Response response;
        try {
            Person u = personEJB.find(personId);
            if (u != null) {
                response = Response.ok(u).build();
            } else {
                response = Response.status(Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = Response.serverError().entity(e.getMessage()).build();
        }
        return response;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePerson(@Valid PersonRequest request) {
        Response response;
        try {
            Person p = new Person(request);
            p.setPersonId(UUID.randomUUID().toString());
            Person created = personEJB.save(p);
            response = Response.status(Status.CREATED).entity(created).build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = Response.serverError().entity(e.getMessage()).build();
        }
        return response;
    }

    @PUT
    @Path(PATH_PERSON_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam(PERSON_ID) String personId, @Valid PersonRequest request) {
        Response response;
        try {
            request.setPersonId(personId);
            Person p = new Person(request);
            p.setIdNumber(personId);
            Person success = personEJB.update(personId, p);
            if (success != null) {
                response = Response.ok(success).build();
            } else {
                response = Response.notModified().build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = Response.serverError().entity(e.getMessage()).build();
        }
        return response;
    }

    @DELETE
    @Path(PATH_PERSON_ID)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePerson(@PathParam(PERSON_ID) String personId) {
        Response response;
        try {
            boolean success = personEJB.delete(personId);
            if (success) {
                response = Response.noContent().build();
            } else {
                response = Response.notModified().build();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response = Response.serverError().entity(e.getMessage()).build();
        }
        return response;
    }

}
