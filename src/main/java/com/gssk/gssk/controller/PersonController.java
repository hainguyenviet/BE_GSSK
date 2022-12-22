package com.gssk.gssk.controller;

import com.gssk.gssk.service.AppUserService;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;




@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/api/person")
public class PersonController {

    @Autowired
    PersonService personService;

    @Autowired
    AppUserService appUserService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<List<Person>> getAllPerson(@RequestParam(defaultValue = "0") Integer pageNo,
                                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Person> list = personService.getAllPerson(pageNo, pageSize);
        return new ResponseEntity<List<Person>>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping(value="/{username}", produces = "application/json")
    public Person person(@PathVariable("username") String username){
        return personService.getPersonByUsername(username);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping(value = "/create", produces = "application/json")
    public Person createPerson(@RequestBody Person person) {
        return personService.addNewPerson(person);
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @DeleteMapping(value = "/delete/{id}")
//    public void deletePerson(@PathVariable("id") Long id) { personService.deletePerson(id); }
    
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping (value = "/total", produces = "application/json")
    public Long countAll(){
        return personService.countAll();
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PutMapping(value = "/update/{id}", produces = "application/json")
    public Person updatePerson(@PathVariable("id") String userID, @RequestBody Person personRequest) { return personService.updatePerson(userID, personRequest); }

}
