package com.gssk.gssk.service;

import com.gssk.gssk.model.Person;
import com.gssk.gssk.repository.PersonRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;


@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    public Iterable<Person> getAllPerson() {
        return personRepository.findAll();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id).get();
    }

    @SneakyThrows
    public Person addNewPerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM tbl_relative WHERE fk_id is null ")
    public Person updatePerson(Long id, Person personRequest){
        Person person = personRepository.findById(id).get();
        person.setFirstName(personRequest.getFirstName());
        person.setLastName(personRequest.getLastName());
        person.setDateOfBirth(person.getDateOfBirth());
        person.setIdCard(personRequest.getIdCard());
        person.setEmail(personRequest.getEmail());
        person.setPhoneNumber(personRequest.getPhoneNumber());
        person.setGender(personRequest.getGender());
        person.setHealthRecord(personRequest.getHealthRecord());

        person.setRelativeList(personRequest.getRelativeList());


        return personRepository.save(person);
    }


    public void deletePerson(Long id){
        Person person = personRepository.findById(id).get();
        personRepository.delete(person);
    }


}
