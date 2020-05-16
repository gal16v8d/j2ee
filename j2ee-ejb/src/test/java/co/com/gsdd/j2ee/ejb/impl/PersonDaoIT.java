package co.com.gsdd.j2ee.ejb.impl;

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.gsdd.j2ee.db.jpa.Person;

@ExtendWith(MockitoExtension.class)
public class PersonDaoIT {

    private EntityManager entityManager;
    private EntityTransaction entityTxn;
    @Spy
    private PersonEJBImpl personDao;
    private static final String DERBY_STREAM_ERROR_FILE = "derby.stream.error.file";
    private static final String TARGET_DERBY_LOG = "target/derby.log";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        System.setProperty(DERBY_STREAM_ERROR_FILE, new File(TARGET_DERBY_LOG).getAbsolutePath());
        entityManager = Persistence.createEntityManagerFactory("j2eeMappingIT").createEntityManager();
        entityTxn = entityManager.getTransaction();
        Mockito.doReturn(entityManager).when(personDao).getManager();
    }

    @Test
    public void saveTest() {
        entityTxn.begin();
        Person response = personDao.save(arrangePerson());
        entityTxn.commit();
        Assertions.assertAll(() -> Assertions.assertNotNull(response),
                () -> Assertions.assertEquals("1234", response.getIdNumber()),
                () -> Assertions.assertEquals("Juan", response.getName()),
                () -> Assertions.assertEquals("Juanete", response.getLastName()));
    }

    private Person arrangePerson() {
        Person p = new Person();
        p.setIdNumber("1234");
        p.setName("Juan");
        p.setLastName("Juanete");
        return p;
    }
}
