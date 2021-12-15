package com.gsdd.j2ee.ejb.impl;

import java.util.Optional;
import javax.ejb.Stateless;
import com.gsdd.j2ee.db.jpa.Person;
import com.gsdd.j2ee.ejb.PersonEJB;

@Stateless
public class PersonEJBImpl extends AbstractCrudEJB<Person, String> implements PersonEJB {

    public PersonEJBImpl() {
        super(Person.class);
    }

    @Override
    public Optional<Person> update(String id, Person entity) {
        return Optional.ofNullable(find(id).map(dbPerson -> overwritePerson(dbPerson, entity)).orElse(null));
    }

    private Person overwritePerson(Person dbPerson, Person newPerson) {
        dbPerson.setIdNumber(newPerson.getIdNumber());
        dbPerson.setName(newPerson.getName());
        dbPerson.setLastName(newPerson.getLastName());
        getManager().merge(dbPerson);
        getManager().flush();
        return dbPerson;
    }

}
