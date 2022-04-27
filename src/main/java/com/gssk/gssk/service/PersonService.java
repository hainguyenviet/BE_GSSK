package com.gssk.gssk.service;

import com.gssk.gssk.model.Person;
import com.gssk.gssk.repository.PersonRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    public Iterable<Person> getAllPerson() {
        return personRepository.findAll();
    }

    public Person getPersonById(String id) {
        return personRepository.findById(id).get();
    }

    @SneakyThrows
    public Person addNewPerson(Person person) {
        return personRepository.save(person);
    }

    public Person updatePerson(String id, Person personRequest){
        Person person = personRepository.findById(id).get();
        person.setFirstName(personRequest.getFirstName());
        person.setLastName(personRequest.getLastName());
        person.setBirthDay(person.getBirthDay());
        person.setIdCard(personRequest.getIdCard());
        person.setEmail(personRequest.getEmail());
        person.setPhoneNumber(personRequest.getPhoneNumber());
        person.setGender(personRequest.getGender());
        person.setHealthRecord(personRequest.getHealthRecord());
        person.setRelativeList(personRequest.getRelativeList());
        return personRepository.save(person);
    }

    public Person addEvaluates(String id, Person personRequest){
        Person person = personRepository.findById(id).get();
        person.setEvaluates(personRequest.getEvaluates());
        return personRepository.save(person);
    }

    public void deletePerson(String id){
        Person person = personRepository.findById(id).get();
        personRepository.delete(person);
    }

}
