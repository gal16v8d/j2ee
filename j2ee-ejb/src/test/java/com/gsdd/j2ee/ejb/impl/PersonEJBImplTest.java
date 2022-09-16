package com.gsdd.j2ee.ejb.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gsdd.j2ee.db.jpa.Person;

@ExtendWith(MockitoExtension.class)
class PersonEJBImplTest {

    private static final String PERSON_ID = "1";
    @Spy
    private PersonEJBImpl personDao;
    @Mock
    private EntityManager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(personDao.getManager()).thenReturn(manager);
    }

    @Test
    void findAllTest(@Mock CriteriaBuilder builder, @Mock CriteriaQuery<Person> criteriaQuery, @Mock Root<Person> root,
            @Mock TypedQuery<Person> typedQuery) {
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
    void findTest() {
        Person p = arrangePerson();
        Mockito.when(manager.find(Person.class, PERSON_ID)).thenReturn(p);
        Optional<Person> response = personDao.find(PERSON_ID);
        Assertions.assertAll(() -> Assertions.assertNotNull(response),
                () -> Assertions.assertNotNull(response.isPresent()), () -> Assertions.assertEquals(p, response.get()));
    }

    @Test
    public void saveTest() {
        Mockito.doNothing().when(manager).persist(Mockito.any(Person.class));
        Person response = personDao.save(arrangePerson());
        Assertions.assertAll(() -> Assertions.assertNotNull(response),
                () -> Assertions.assertEquals("1234", response.getIdNumber()),
                () -> Assertions.assertEquals("Juan", response.getName()),
                () -> Assertions.assertEquals("Juanete", response.getLastName()));
        Mockito.verify(manager).persist(Mockito.any(Person.class));
    }

    @Test
    void updateNotFoundTest(@Mock Person person) {
        Mockito.when(personDao.find(PERSON_ID)).thenReturn(Optional.empty());
        Assertions.assertFalse(personDao.update(PERSON_ID, person).isPresent());
    }

    @Test
    void updateTest() {
        Person pr = arrangePerson();
        pr.setPersonId(PERSON_ID);
        Mockito.doReturn(Optional.ofNullable(pr)).when(personDao).find(PERSON_ID);
        Mockito.when(manager.merge(Mockito.any(Person.class))).thenReturn(pr);
        Optional<Person> response = personDao.update(PERSON_ID, pr);
        Assertions.assertAll(() -> Assertions.assertNotNull(response),
                () -> Assertions.assertTrue(response.isPresent()),
                () -> Assertions.assertEquals(pr.getIdNumber(), response.get().getIdNumber()));
    }

    @Test
    void deleteNotFoundTest(@Mock Person person) {
        Mockito.when(personDao.find(PERSON_ID)).thenReturn(Optional.empty());
        Assertions.assertFalse(personDao.delete(PERSON_ID));
    }

    @Test
    void deleteFoundTest(@Mock Person person) {
        Mockito.doReturn(Optional.ofNullable(person)).when(personDao).find(PERSON_ID);
        Mockito.doNothing().when(manager).remove(person);
        Assertions.assertTrue(personDao.delete(PERSON_ID));
    }

    private Person arrangePerson() {
        Person p = new Person();
        p.setIdNumber("1234");
        p.setName("Juan");
        p.setLastName("Juanete");
        return p;
    }
}
