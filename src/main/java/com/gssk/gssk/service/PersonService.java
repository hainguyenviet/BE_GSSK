package com.gssk.gssk.service;

import com.gssk.gssk.model.Person;
import com.gssk.gssk.repository.PersonRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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




}
