package com.gssk.gssk.controller;

import com.gssk.gssk.model.Person;
import com.gssk.gssk.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("person")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping
    public List<Person> getAllPerson() {
        return (List<Person>) personService.getAllPerson();
    }

    @GetMapping(value="/{id}", produces = "application/json")
    public Person person(@PathVariable("id") String id){
        return personService.getPersonById(id);
    }

    @PostMapping( produces = "application/json")
    public Person createPerson(@RequestBody Person person) {
        return personService.addNewPerson(person);
    }

}
