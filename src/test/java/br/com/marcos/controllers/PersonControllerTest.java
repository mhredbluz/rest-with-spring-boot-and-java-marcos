package br.com.marcos.controllers;
import br.com.marcos.exceptions.ResourceNotFoundException;
import br.com.marcos.model.Person;
import br.com.marcos.services.PersonServices;import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.management.relation.RelationServiceNotRegisteredException;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PersonServices services;

    private Person person;

    @BeforeEach
    public void setup() {
        person = new Person("Marcos", "Dutra", "Uberlândia - MG - Brasil", "Male", "mhredbluz@gmail.com");
    }

    @DisplayName("JUnity test Given Person Object when Create Person then Return Saved Person")
    @Test
    void testGivenPersonObject_whenCreatePerson_thenReturnSavedPerson() throws Exception {
        //Given / Arrange
        given(services.create(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        //When / Act
        ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)));
        //Then / Assert
        response.andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @DisplayName("JUnity test Given List Persons when findAll Persons then Return Person Lists")
    @Test
    void testGivenListOfPersons_whenFindAllPersons_thenReturnPersonsList() throws Exception {

        //Given / Arrange
        List<Person> persons = new ArrayList<>();
        persons.add(person);
        persons.add(new Person(
                "André",
                "Pereira",
                "São Paulo",
                "Male",
                "andrep@hotmail.com"));

        given(services.findAll()).willReturn(persons);

        //When / Act
        ResultActions response = mockMvc.perform(get("/person"));
        //Then / Assert
        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(persons.size())));
    }

    @DisplayName("JUnity test Given Person Id when findById then Return Person Object")
    @Test
    void testGivenPersonId_whenFindById_thenReturnPersonObject() throws Exception {
        //Given / Arrange
        long personId = 1L;
        given(services.findById(personId)).willReturn(person);

        //When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));
        //Then / Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }
    @DisplayName("JUnity test Given invalid Person Id when findById then Return Not Found")
    @Test
    void testGivenInvalidPersonId_whenFindById_thenReturnNotFound() throws Exception {
        //Given / Arrange
        long personId = 1L;
        given(services.findById(personId)).willThrow(ResourceNotFoundException.class);

        //When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));
        //Then / Assert
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
    @DisplayName("JUnity test Given Update Person when Update then Return Updated Person Object")
    @Test
    void testGivenUpdatePersonId_whenUpdate_thenReturnUpdatedPersonObject() throws Exception {
        //Given / Arrange
        long personId = 1L;
        given(services.findById(personId)).willReturn(person);
        given(services.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
        Person updatedPerson = new Person(
                "André",
                "Pereira",
                "São Paulo",
                "Male",
                "andrep@hotmail.com");

        //When / Act
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));
        //Then / Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andDo(print()).andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }
    @DisplayName("JUnity test Given Unexistent Person when Update then Return Not Found")
    @Test
    void testGivenUnexistentPerson_whenUpdate_thenReturnNotFound() throws Exception {
        //Given / Arrange
        long personId = 1L;
        given(services.findById(personId)).willThrow(ResourceNotFoundException.class);
        given(services.update(any(Person.class)))
                .willAnswer((invocation) -> invocation.getArgument(1));

        //When / Act
        ResultActions response = mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)));
        //Then / Assert
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
    @DisplayName("JUnity test Given Person id when Delete then Return No Content")
    @Test
    void testGivenPersonId_whenDelete_thenReturnNoContent() throws Exception {
        //Given / Arrange
        long personId = 1L;
        willDoNothing().given(services).delete(personId);

        //When / Act
        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));
        //Then / Assert
        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}
