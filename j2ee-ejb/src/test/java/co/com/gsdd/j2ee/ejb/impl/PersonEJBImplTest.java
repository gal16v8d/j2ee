package co.com.gsdd.j2ee.ejb.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import co.com.gsdd.j2ee.db.jpa.Person;

public class PersonEJBImplTest {

    private static final String PERSON_ID = "1";
    @Spy
    private PersonEJBImpl personDao;
    @Mock
    private EntityManager manager;
    @Mock
    private CriteriaBuilder builder;
    @Mock
    private CriteriaQuery<Person> criteriaQuery;
    @Mock
    private Root<Person> root;
    @Mock
    private TypedQuery<Person> typedQuery;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(personDao.getManager()).thenReturn(manager);
    }

    @Test
    public void findAllTest() {
        List<Person> lp = new ArrayList<>();
        Mockito.when(manager.getCriteriaBuilder()).thenReturn(builder);
        Mockito.when(builder.createQuery(Person.class)).thenReturn(criteriaQuery);
        Mockito.when(criteriaQuery.from(Person.class)).thenReturn(root);
        Mockito.when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
        Mockito.when(manager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        Mockito.when(typedQuery.getResultList()).thenReturn(lp);
        List<Person> response = personDao.findAll();
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    public void findTest() {
        Person p = arrangePerson();
        Mockito.when(manager.find(Person.class, PERSON_ID)).thenReturn(p);
        Person response = personDao.find(PERSON_ID);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(p, response);
    }

    @Test
    public void saveTest() {
        Mockito.doNothing().when(manager).persist(Mockito.any(Person.class));
        Person response = personDao.save(arrangePerson());
        Assertions.assertNotNull(response);
        Assertions.assertEquals("1234", response.getIdNumber());
        Assertions.assertEquals("Juan", response.getName());
        Assertions.assertEquals("Juanete", response.getLastName());
        Mockito.verify(manager).persist(Mockito.any(Person.class));
    }

    @Test
    public void updateNotFoundTest() {
        Mockito.when(personDao.find(PERSON_ID)).thenReturn(null);
        Person p = Mockito.mock(Person.class);
        Mockito.when(p.getPersonId()).thenReturn(PERSON_ID);
        Person response = personDao.update(PERSON_ID, p);
        Assertions.assertEquals(null, response);
    }

    @Test
    public void updateTest() {
        Person pr = arrangePerson();
        pr.setPersonId(PERSON_ID);
        Mockito.when(personDao.find(PERSON_ID)).thenReturn(pr);
        Mockito.when(manager.merge(Mockito.any(Person.class))).thenReturn(pr);
        Person response = personDao.update(PERSON_ID, pr);
        Assertions.assertEquals(pr.getIdNumber(), response.getIdNumber());
    }

    @Test
    public void deleteNotFoundTest() {
        Person person = Mockito.mock(Person.class);
        Mockito.when(personDao.find(PERSON_ID)).thenReturn(null);
        Mockito.when(person.getPersonId()).thenReturn(PERSON_ID);
        boolean response = personDao.delete(PERSON_ID);
        Assertions.assertFalse(response);
    }

    @Test
    public void deleteFoundTest() {
        Person person = Mockito.mock(Person.class);
        Mockito.when(person.getPersonId()).thenReturn(PERSON_ID);
        Mockito.when(personDao.find(PERSON_ID)).thenReturn(person);
        Mockito.doNothing().when(manager).remove(person);
        boolean response = personDao.delete(PERSON_ID);
        Assertions.assertTrue(response);
    }

    private Person arrangePerson() {
        Person p = new Person();
        p.setIdNumber("1234");
        p.setName("Juan");
        p.setLastName("Juanete");
        return p;
    }
}
