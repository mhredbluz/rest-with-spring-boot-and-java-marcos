package br.com.marcos.services;

import br.com.marcos.exceptions.ResourceNotFoundException;
import br.com.marcos.model.Person;
import br.com.marcos.respositories.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    @Mock
    private PersonRepository repository;
    @InjectMocks
    private PersonServices services;
    private Person person0;

    @BeforeEach
    public void setup(){
        person0 = new Person(
                "Marcos",
                "Dutra",
                "Uberlândia - MG - Brasil",
                "Male",
                "mhredbluz@gmail.com"
        );
    }

     @DisplayName("JUnit test Given Person Object when Save Person then Return Person Object")
     @Test
     void testGivenPersonObject_whenSavePerson_thenReturnPersonObject() {
         //Given / Arrange
         given(repository.findByEmail(anyString())).willReturn(Optional.empty());
         given(repository.save(person0)).willReturn(person0);
         //When / Act
         Person savedPerson = services.create(person0);
         //Then / Assert
         assertNotNull(savedPerson);
         assertEquals("Marcos", savedPerson.getFirstName());
     }
     @DisplayName("JUnit test Given Existing Email when Save Person then Throws Exception")
     @Test
     void testGivenExistingEmail_whenSavePerson_thenThrowsException() {
         //Given / Arrange
         given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));
         //When / Act
         assertThrows(ResourceNotFoundException.class, () -> {
             services.create(person0);
         });
         //Then / Assert
         verify(repository, never()).save(any(Person.class));
     }
     @DisplayName("JUnit test Given Person list when findAll Persons then return person list")
     @Test
     void testGivenPersonList_whenFindAllPersons_thenReturnPersonsList() {
         //Given / Arrange
         Person person1 = new Person(
                 "Henrique",
                 "Soares",
                 "Uberlândia - MG - Brasil",
                 "Male",
                 "mhredbluz@gmail.com"
         );

         given(repository.findAll()).willReturn(List.of(person0, person1));
         //When / Act
         List<Person> personList = services.findAll();
         //Then / Assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }
    @DisplayName("JUnit test Given Empty Person list when findAll Persons then return Empty person list")
     @Test
     void testGivenEmptyPersonList_whenFindAllPersons_thenReturnEmptyPersonsList() {
         //Given / Arrange

         given(repository.findAll()).willReturn(Collections.emptyList());
         //When / Act
         List<Person> personList = services.findAll();
         //Then / Assert
        assertTrue(personList.isEmpty());
        assertEquals(0, personList.size());
    }
    @DisplayName("JUnit test Given Person Id when findById then Return Object")
    @Test
    void testGivenPersonId_whenFindById_thenReturnObject() {
        //Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        //When / Act
        Person savedPerson = services.findById(1L);
        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Marcos", savedPerson.getFirstName());
    }
    @DisplayName("JUnit test Given Person Object when Update Person then Return updated person Object")
    @Test
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {
        //Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        person0.setFirstName("Hugo");
        person0.setEmail("hugo@gmail.com.br");

        given(repository.save(person0)).willReturn(person0);

        //When / Act
        Person updatedPerson = services.update(person0);
        //Then / Assert
        assertNotNull(updatedPerson);
        assertEquals("Hugo", updatedPerson.getFirstName());
        assertEquals("hugo@gmail.com.br", updatedPerson.getEmail());
    }
    @DisplayName("JUnit test Given Person Id when delete Person then do Nothing ")
    @Test
    void testGivenPersonId_whenDeletePerson_thenDoNothing() {
        //Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        willDoNothing().given(repository).delete(person0);

        //When / Act
        services.delete(person0.getId());
        //Then / Assert
        verify(repository, times(1)).delete(person0);
    }
}

