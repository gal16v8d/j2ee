package co.com.gsdd.j2ee.rest.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import co.com.gsdd.j2ee.db.jpa.Person;
import co.com.gsdd.j2ee.ejb.PersonEJB;
import co.com.gsdd.j2ee.rest.api.request.PersonRequest;

public class PersonRestTest {

    private static final String TEST_PERSON_ID = "1";
    private static final String MOCKED_EXCEPTION = "Mocked Exception";
    @InjectMocks
    private PersonRest personRest;
    @Mock
    private PersonEJB personEJB;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @ParameterizedTest
    @MethodSource("nullOrEmptyPersonListInput")
    public void getAllPersonsNotFoundTest(List<Person> persons) {
        Mockito.when(personEJB.findAll()).thenReturn(persons);
        Response response = personRest.getAllPersons();
        assertStatus(response, Status.NOT_FOUND);
    }

    private static Stream<Arguments> nullOrEmptyPersonListInput() {
        return Stream.of(null, Arguments.of(Collections.emptyList()));
    }

    @Test
    public void getAllPersonsExcTest() {
        Mockito.when(personEJB.findAll()).thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.getAllPersons();
        assertServerError(response);
        Assertions.assertEquals(MOCKED_EXCEPTION, response.getEntity());
    }

    @Test
    public void getAllPersonsTest() {
        List<Person> lp = new ArrayList<>();
        lp.add(arrangePerson());
        Mockito.when(personEJB.findAll()).thenReturn(lp);
        Response response = personRest.getAllPersons();
        assertStatus(response, Status.OK);
        Assertions.assertTrue(response.getEntity() instanceof List);
    }

    @Test
    public void getPersonNullTest() {
        Mockito.when(personEJB.find(TEST_PERSON_ID)).thenReturn(null);
        Response response = personRest.getPerson(TEST_PERSON_ID);
        assertStatus(response, Status.NOT_FOUND);
    }

    @Test
    public void getPersonExcTest() {
        Mockito.when(personEJB.find(TEST_PERSON_ID)).thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.getPerson(TEST_PERSON_ID);
        assertServerError(response);
        Assertions.assertEquals(MOCKED_EXCEPTION, response.getEntity());
    }

    @Test
    public void getPersonTest() {
        Person p = arrangePerson();
        Mockito.when(personEJB.find(TEST_PERSON_ID)).thenReturn(p);
        Response response = personRest.getPerson(TEST_PERSON_ID);
        assertStatus(response, Status.OK);
        Assertions.assertTrue(response.getEntity() instanceof Person);
    }

    @Test
    public void savePersonExcTest() {
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.save(Mockito.any(Person.class))).thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.savePerson(pr);
        assertServerError(response);
    }

    @Test
    public void savePersonTest() {
        Person p = arrangePerson();
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.save(Mockito.any(Person.class))).thenReturn(p);
        Response response = personRest.savePerson(pr);
        assertStatus(response, Status.CREATED);
    }

    @Test
    public void updatePersonTest() {
        Person p = arrangePerson();
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.update(Mockito.anyString(), Mockito.any(Person.class))).thenReturn(p);
        Response response = personRest.updateUser(TEST_PERSON_ID, pr);
        assertStatus(response, Status.OK);
        Assertions.assertTrue(response.getEntity() instanceof Person);
    }

    @Test
    public void updatePersonNotModifiedTest() {
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.update(Mockito.anyString(), Mockito.any(Person.class))).thenReturn(null);
        Response response = personRest.updateUser(TEST_PERSON_ID, pr);
        assertStatus(response, Status.NOT_MODIFIED);
    }

    @Test
    public void updatePersonExcTest() {
        PersonRequest pr = new PersonRequest();
        Mockito.when(personEJB.update(Mockito.anyString(), Mockito.any(Person.class)))
                .thenThrow(new RuntimeException(MOCKED_EXCEPTION));
        Response response = personRest.updateUser(TEST_PERSON_ID, pr);
        assertServerError(response);
        Assertions.assertEquals(MOCKED_EXCEPTION, response.getEntity());
    }

    @Test
    public void deletePersonNoContentTest() {
        Mockito.when(personEJB.delete(TEST_PERSON_ID)).thenReturn(true);
        Response response = personRest.deletePerson(TEST_PERSON_ID);
        assertStatus(response, Status.NO_CONTENT);
    }

    @Test
    public void deletePersonNotModifiedTest() {
        Mockito.when(personEJB.delete(TEST_PERSON_ID)).thenReturn(false);
        Response response = personRest.deletePerson(TEST_PERSON_ID);
        assertStatus(response, Status.NOT_MODIFIED);
    }

    @Test
    public void deletePersonExcTest() {
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
