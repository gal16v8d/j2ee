package co.com.gsdd.j2ee.rest.api.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import co.com.gsdd.j2ee.db.jpa.Person;
import co.com.gsdd.j2ee.ejb.PersonEJB;
import co.com.gsdd.j2ee.rest.api.request.PersonRequest;

@ExtendWith(MockitoExtension.class)
class PersonRestImplTest {

    private static final String TEST_PERSON_ID = "1";
    private static final String MOCKED_EXCEPTION = "Mocked Exception";
    @InjectMocks
    private PersonRestImpl personRest;
    @Mock
    private PersonEJB personEJB;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyPersonListInput")
    void getAllPersonsNotFoundTest(List<Person> persons) {
        Mockito.when(personEJB.findAll()).thenReturn(persons);
        Response response = personRest.getAllPersons();
        assertStatus(response, Status.NOT_FOUND);
    }

    private static Stream<Arguments> nullOrEmptyPersonListInput() {
        return Stream.of(null, Arguments.of(Collections.emptyList()));
    }

    @Test
    void getAllPersonsExcTest() {
        Mockito.when(personEJB.findAll()).thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.getAllPersons();
        assertServerError(response);
        Assertions.assertEquals(MOCKED_EXCEPTION, response.getEntity());
    }

    @Test
    void getAllPersonsTest() {
        List<Person> lp = new ArrayList<>();
        lp.add(arrangePerson());
        Mockito.when(personEJB.findAll()).thenReturn(lp);
        Response response = personRest.getAllPersons();
        assertStatus(response, Status.OK);
        Assertions.assertTrue(response.getEntity() instanceof List);
    }

    @Test
    void getPersonNullTest() {
        Mockito.when(personEJB.find(TEST_PERSON_ID)).thenReturn(Optional.empty());
        Response response = personRest.getPerson(TEST_PERSON_ID);
        assertStatus(response, Status.NOT_FOUND);
    }

    @Test
    void getPersonExcTest() {
        Mockito.when(personEJB.find(TEST_PERSON_ID)).thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.getPerson(TEST_PERSON_ID);
        assertServerError(response);
        Assertions.assertEquals(MOCKED_EXCEPTION, response.getEntity());
    }

    @Test
    void getPersonTest() {
        Mockito.when(personEJB.find(TEST_PERSON_ID)).thenReturn(Optional.ofNullable(arrangePerson()));
        Response response = personRest.getPerson(TEST_PERSON_ID);
        assertStatus(response, Status.OK);
        Assertions.assertTrue(response.getEntity() instanceof Person);
    }

    @Test
    void savePersonExcTest() {
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.save(Mockito.any(Person.class))).thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.savePerson(pr);
        assertServerError(response);
    }

    @Test
    void savePersonTest() {
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.save(Mockito.any(Person.class))).thenReturn(arrangePerson());
        Response response = personRest.savePerson(pr);
        assertStatus(response, Status.CREATED);
    }

    @Test
    void updatePersonTest() {
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.update(Mockito.anyString(), Mockito.any(Person.class)))
                .thenReturn(Optional.ofNullable(arrangePerson()));
        Response response = personRest.updateUser(TEST_PERSON_ID, pr);
        assertStatus(response, Status.OK);
        Assertions.assertTrue(response.getEntity() instanceof Person);
    }

    @Test
    void updatePersonNotModifiedTest() {
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.update(Mockito.anyString(), Mockito.any(Person.class))).thenReturn(Optional.empty());
        Response response = personRest.updateUser(TEST_PERSON_ID, pr);
        assertStatus(response, Status.NOT_MODIFIED);
    }

    @Test
    void updatePersonExcTest() {
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.update(Mockito.anyString(), Mockito.any(Person.class)))
                .thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.updateUser(TEST_PERSON_ID, pr);
        assertServerError(response);
        Assertions.assertEquals(MOCKED_EXCEPTION, response.getEntity());
    }

    @Test
    void deletePersonNoContentTest() {
        Mockito.when(personEJB.delete(TEST_PERSON_ID)).thenReturn(true);
        Response response = personRest.deletePerson(TEST_PERSON_ID);
        assertStatus(response, Status.NO_CONTENT);
    }

    @Test
    void deletePersonNotModifiedTest() {
        Mockito.when(personEJB.delete(TEST_PERSON_ID)).thenReturn(false);
        Response response = personRest.deletePerson(TEST_PERSON_ID);
        assertStatus(response, Status.NOT_MODIFIED);
    }

    @Test
    void deletePersonExcTest() {
        Mockito.when(personEJB.delete(TEST_PERSON_ID)).thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.deletePerson(TEST_PERSON_ID);
        assertServerError(response);
        Assertions.assertEquals(MOCKED_EXCEPTION, response.getEntity());
    }

    private void assertServerError(Response response) {
        assertStatus(response, Status.INTERNAL_SERVER_ERROR);
    }

    private void assertStatus(Response response, Status status) {
        Assertions.assertNotNull(response);
        Assertions.assertEquals(status.getStatusCode(), response.getStatus());
    }

    private Person arrangePerson() {
        Person p = new Person();
        p.setIdNumber("1234");
        p.setName("Juan");
        p.setLastName("Juanete");
        return p;
    }
}
