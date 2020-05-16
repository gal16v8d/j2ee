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
			response = personEJB.find(personId).map(dbPerson -> Response.ok(dbPerson).build())
					.orElseGet(() -> Response.status(Status.NOT_FOUND).build());
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
			response = Response.status(Status.CREATED).entity(personEJB.save(p)).build();
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
			response = personEJB.update(personId, p).map(updated -> Response.ok(updated).build())
					.orElseGet(() -> Response.notModified().build());
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
