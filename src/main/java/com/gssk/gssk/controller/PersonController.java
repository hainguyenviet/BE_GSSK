package com.gssk.gssk.controller;

import com.gssk.gssk.account.AppUserService;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @Autowired
    AppUserService appUserService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/all", produces = "application/json")
    public List<Person> getAllPerson() {
        return (List<Person>) personService.getAllPerson();
    }

    @PreAuthorize(("hasRole('USER') or hasRole('ADMIN')" ))
    @GetMapping(value="/{id}", produces = "application/json")
    public Person person(@PathVariable("id") Long id){
        return personService.getPersonById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/create", produces = "application/json")
    public Person createPerson(@RequestBody Person person) {
        return personService.addNewPerson(person);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public void deletePerson(@PathVariable("id") Long id) { personService.deletePerson(id); }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping(value = "/update/{id}", produces = "application/json")
    public Person updatePerson(@PathVariable("id") Long id, Person personRequest) { return personService.updatePerson(id, personRequest); }



}
