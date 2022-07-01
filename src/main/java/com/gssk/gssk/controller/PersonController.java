package com.gssk.gssk.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gssk.gssk.account.AppUser;
import com.gssk.gssk.account.AppUserService;
import com.gssk.gssk.account.ERole;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


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
    public List<Person> getAllPerson() {
        return (List<Person>) personService.getAllPerson();
    }

    @PreAuthorize(("hasAuthority('ADMIN')" ))
    @GetMapping(value="/{id}", produces = "application/json")
    public Person person(@PathVariable("id") Long id){
        return personService.getPersonById(id);
    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping(value = "/create", produces = "application/json")
    public Person createPerson(@RequestBody Person person) {
        return personService.addNewPerson(person);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public void deletePerson(@PathVariable("id") Long id) { personService.deletePerson(id); }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PutMapping(value = "/update/{id}", produces = "application/json")
    public Person updatePerson(@PathVariable("id") Long id, @RequestBody Person personRequest) { return personService.updatePerson(id, personRequest); }

}
