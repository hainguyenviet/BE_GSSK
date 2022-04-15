package com.gssk.gssk.service;

import com.gssk.gssk.model.Person;
import com.gssk.gssk.repository.PersonRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public Person updatePerson(Person person, String field,List<String> item)
    {
        Person newPerson=person;

        switch (field)
        {
            case "firstName":newPerson.setFirstName(item.get(0));break;
            case "lastName":newPerson.setLastName(item.get(0));break;
            case "idCard":newPerson.setIdCard(item.get(0));break;
            case "email":newPerson.setEmail(item.get(0));break;
            case "gender":newPerson.setGender(item.get(0));break;
            case "phonenumber":newPerson.setPhoneNumber(item.get(0));break;
            case "birthday":{
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Date updateBirthday= null;
                try {
                    updateBirthday = formatter.parse(item.get(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (updateBirthday !=null)
                    newPerson.setBirthDay(updateBirthday);
            };break;
        }
        return newPerson;
    }


    @SneakyThrows
    public Person addNewPerson(Person person) {
        return personRepository.save(person);
    }




}
