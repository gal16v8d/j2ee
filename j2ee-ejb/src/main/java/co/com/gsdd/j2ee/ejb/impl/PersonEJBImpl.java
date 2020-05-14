package co.com.gsdd.j2ee.ejb.impl;

import javax.ejb.Stateless;

import co.com.gsdd.j2ee.db.jpa.Person;
import co.com.gsdd.j2ee.ejb.PersonEJB;

@Stateless
public class PersonEJBImpl extends AbstractCrudEJB<Person, String> implements PersonEJB {

	public PersonEJBImpl() {
		super(Person.class);
	}

	@Override
	public Person update(String id, Person entity) {
		Person dbEntity = find(id);
		if (dbEntity != null) {
			dbEntity.setIdNumber(entity.getIdNumber());
			dbEntity.setName(entity.getName());
			dbEntity.setLastName(entity.getLastName());
			getManager().merge(dbEntity);
			getManager().flush();
		}
		return dbEntity;
	}

}
