package br.com.marcos.repositories;

import br.com.marcos.integrationtests.containers.AbstractIntegrationTest;
import br.com.marcos.model.Person;
import br.com.marcos.respositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository repository;

    private Person person0;

    @BeforeEach
    public void setup(){
        //Given / Arrange
        person0 = new Person(
                "Marcos",
                "Dutra",
                "Uberlândia - Minas Gerais - Brasil",
                "Male",
                "mhredbluz@gmail.com");
    }

    @DisplayName("JUnity Test for Given Person Object when Save then Return Saved Person")
    @Test
    void testGivenPersonObject_whenSave_thenReturnSavedPerson() {

        //When / Act
        Person savedPerson = repository.save(person0);

        //Then / Assert
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }
    @DisplayName("JUnity Test for Given Person List when findAll then Return Saved Person")
    @Test
    void testGivenPersonList_whenFindAll_thenReturnSavedPerson() {

        Person person1 = new Person("Angêlo",
                "Soares",
                "Uberlândia - Minas Gerais - Brasil",
                "Male",
                "mhredbluz@gmail.com");

        repository.save(person0);
        repository.save(person1);

        //When / Act
        List<Person> personList = repository.findAll();

        //Then / Assert
        assertNotNull(personList);
    }
    @DisplayName("JUnity Test for Given Person Object when findById then Return Saved Person Object")
    @Test
    void testGivenPersonObject_whenFindById_thenReturnPersonObject() {

        repository.save(person0);
        //When / Act
        Person savedPerson = repository.findById(person0.getId()).get();

        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person0.getId(), savedPerson.getId());
    }

    @DisplayName("JUnity Test for Given Person Object when findByEmail then Return Saved Person Object")
    @Test
    void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {

        repository.save(person0);
        //When / Act
        Person savedPerson = repository.findByEmail(person0.getEmail()).get();

                //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person0.getEmail(), savedPerson.getEmail());
    }
    @DisplayName("JUnity Test for Given Person Object when Update Person then Return Updated Person Object")
    @Test
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {

        repository.save(person0);
        //When / Act
        Person savedPerson = repository.findById(person0.getId()).get();
        savedPerson.setFirstName("André");
        savedPerson.setEmail("andre@gmail.com");

        Person updatedPerson = repository.save(savedPerson);

        //Then / Assert
        assertNotNull(updatedPerson);
        assertEquals(updatedPerson.getFirstName(), savedPerson.getFirstName());
        assertEquals(updatedPerson.getEmail(), savedPerson.getEmail());
    }

    @DisplayName("JUnity Test for Given Person Object when Delete then Remove Person")
    @Test
    void testGivenPersonObject_whenDelete_thenRemovePerson() {

        repository.save(person0);

        //When / Act
        repository.deleteById(person0.getId());

        Optional<Person> personOptional = repository.findById(person0.getId());

        //Then / Assert
        assertTrue(personOptional.isEmpty());
    }

    @DisplayName("JUnity Test for Given firstName And lastName when findJPQL then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindJPQL_thenReturnPersonObject() {

        repository.save(person0);
        String firstName = "Marcos";
        String lastName = "Dutra";

        //When / Act
        Person savedPerson = repository.findByJPQL(firstName, lastName);

        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }

    @DisplayName("JUnity Test for Given firstName And lastName when findJPQLNamedParameters then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindJPQLNamedParameters_thenReturnPersonObject() {

        repository.save(person0);
        String firstName = "Marcos";
        String lastName = "Dutra";

        //When / Act
        Person savedPerson = repository.findByJPQLNamedParameters(firstName, lastName);

        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }
    @DisplayName("JUnity Test for Given firstName And lastName when findNativeSQL then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindNativeSQL_thenReturnPersonObject() {

        repository.save(person0);
        String firstName = "Marcos";
        String lastName = "Dutra";

        //When / Act
        Person savedPerson = repository.findByJPQLNamedParameters(firstName, lastName);

        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }
    @DisplayName("JUnity Test for Given firstName And lastName when findNativeSQL with Named Parameters then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindNativeSQLWhitNamedParameters_thenReturnPersonObject() {

        repository.save(person0);
        String firstName = "Marcos";
        String lastName = "Dutra";

        //When / Act
        Person savedPerson = repository.findByJPQLNamedParameters(firstName, lastName);

        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }
}
