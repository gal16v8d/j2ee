package co.com.gsdd.j2ee.rest.api.impl;

import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import co.com.gsdd.j2ee.db.jpa.Person;
import co.com.gsdd.j2ee.ejb.PersonEJB;
import co.com.gsdd.j2ee.ejb.PersonProxy;
import co.com.gsdd.j2ee.rest.api.PersonRest;
import co.com.gsdd.j2ee.rest.api.request.PersonRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonRestImpl implements PersonRest {

    @EJB
    private PersonEJB personEJB;
    @Inject
    private PersonProxy personProxy;

    @Override
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

    @Override
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

    @Override
    public Response getPerson(String personId) {
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

    @Override
    public Response savePerson(PersonRequest request) {
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

    @Override
    public Response updateUser(String personId, PersonRequest request) {
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

    @Override
    public Response deletePerson(String personId) {
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
