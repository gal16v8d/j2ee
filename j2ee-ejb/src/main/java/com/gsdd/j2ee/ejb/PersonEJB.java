package com.gsdd.j2ee.ejb;

import javax.ejb.Local;
import com.gsdd.j2ee.db.jpa.Person;

@Local
public interface PersonEJB extends ICrud<Person, String> {

}
