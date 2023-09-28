package br.com.marcos.integrationtests.controller;

import br.com.marcos.config.TestConfigs;
import br.com.marcos.integrationtests.containers.AbstractIntegrationTest;
import br.com.marcos.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerIntegrationTest extends AbstractIntegrationTest {

   private static RequestSpecification specification;
   private static ObjectMapper objectMapper;
   private static Person person;

   @BeforeAll
   public static void setup() {
       objectMapper = new ObjectMapper();
       objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

       specification = new RequestSpecBuilder()
               .setBasePath("/person")
               .setPort(TestConfigs.SERVER_PORT)
               .addFilter(new RequestLoggingFilter(LogDetail.ALL))
               .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
               .build();

       person = new Person("Marcos", "Dutra", "Uberlândia - MG - Brasil", "Male", "mhredbluz@gmail.com");
   }
    @Order(1)
    @DisplayName("JUnit Integration Test when Create One Person Should Return A Person Object")
    @Test
    void testIntegrationTest_whenCreateOnePerson_ShouldReturnAPersonObject() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getEmail());

        assertTrue(createdPerson.getId() > 0);
        assertEquals("Marcos", createdPerson.getFirstName());
        assertEquals("Dutra", createdPerson.getLastName());
        assertEquals("Uberlândia - MG - Brasil" ,createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertEquals("mhredbluz@gmail.com", createdPerson.getEmail());
    }
    @Order(2)
    @DisplayName("JUnit Integration Test given Person Object when Update One Person Should Return A Updated Person Object")
    @Test
    void integrationTestGivenPersonObject_when_UpdateOnePerson_SholdReturnAUpdatedPersonObject() throws JsonProcessingException {

       person.setFirstName("Leandro");
       person.setEmail("leandro@gmail.com");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person createdPerson = objectMapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getEmail());

        assertTrue(createdPerson.getId() > 0);
        assertEquals("Leandro", createdPerson.getFirstName());
        assertEquals("Dutra", createdPerson.getLastName());
        assertEquals("Uberlândia - MG - Brasil" ,createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertEquals("leandro@gmail.com", createdPerson.getEmail());
    }
    @Order(3)
    @DisplayName("JUnit Integration Test given Person Object when findById Person Should Return A Person Object")
    @Test
    void integrationTestGivenPersonObject_when_findById_ShouldReturnAPersonObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .pathParam("id", person.getId())
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person foundPerson = objectMapper.readValue(content, Person.class);

        person = foundPerson;

        assertNotNull(foundPerson);
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
        assertNotNull(foundPerson.getEmail());

        assertTrue(foundPerson.getId() > 0);
        assertEquals("Leandro", foundPerson.getFirstName());
        assertEquals("Dutra", foundPerson.getLastName());
        assertEquals("Uberlândia - MG - Brasil" ,foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertEquals("leandro@gmail.com", foundPerson.getEmail());
    }
    @Order(4)
    @DisplayName("JUnit Integration when findAll Person Should Return All Person")
    @Test
    void integrationTest_when_findAll_ShouldReturnAllPerson() throws JsonProcessingException {

        Person anotherPerson = new Person("Alicia", "Alves", "Uberlândia - MG - Brasil", "Male", "alicialas@gmail.com");

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(anotherPerson)
                .when()
                .post()
                .then()
                .statusCode(200);

        var content = given().spec(specification)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        Person[] myArray = objectMapper.readValue(content, Person[].class);
        List<Person> people = Arrays.asList(myArray);

        Person foundPersonOne = people.get(0);
        person = foundPersonOne;

        assertNotNull(foundPersonOne);
        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertNotNull(foundPersonOne.getEmail());

        assertTrue(foundPersonOne.getId() > 0);
        assertEquals("Leandro", foundPersonOne.getFirstName());
        assertEquals("Dutra", foundPersonOne.getLastName());
        assertEquals("Uberlândia - MG - Brasil" ,foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());
        assertEquals("leandro@gmail.com", foundPersonOne.getEmail());

        Person foundPersonTwo = people.get(1);
        person = foundPersonTwo;

        assertNotNull(foundPersonTwo);
        assertNotNull(foundPersonTwo.getId());
        assertNotNull(foundPersonTwo.getFirstName());
        assertNotNull(foundPersonTwo.getLastName());
        assertNotNull(foundPersonTwo.getAddress());
        assertNotNull(foundPersonTwo.getGender());
        assertNotNull(foundPersonTwo.getEmail());

        assertTrue(foundPersonTwo.getId() > 0);
        assertEquals("Alicia", foundPersonTwo.getFirstName());
        assertEquals("Alves", foundPersonTwo.getLastName());
        assertEquals("Uberlândia - MG - Brasil" ,foundPersonTwo.getAddress());
        assertEquals("Male", foundPersonTwo.getGender());
        assertEquals("alicialas@gmail.com", foundPersonTwo.getEmail());
    }

    @Order(5)
    @DisplayName("JUnit Integration Test given Person Object when Delete Person Should Return No Content")
    @Test
    void integrationTestGivenPersonObject_when_Delete_ShouldReturnNoContent() throws JsonProcessingException {

        var content = given().spec(specification)
                .pathParam("id", person.getId())
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

}
