package com.gssk.gssk.controller;


import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.service.GenogramService;
import com.gssk.gssk.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("geno")
public class GenogramController {
    @Autowired
    GenogramService genogramService;
    PersonService personService;

    @GetMapping
    public List<Genogram> getAllGenogram(){return (List<Genogram>)genogramService.getAllNodes();}

    @GetMapping(value="/{id}", produces = "application/json")
    public Genogram genogram(@PathVariable("id") String id) {return genogramService.FindByID(id);}

    @PostMapping(value = "cv/{id}", produces = "application/json")
    public Genogram generateGenofromID(@PathVariable("id") String id){
        Person person=personService.getPersonById(id);
        Genogram genogram = new Genogram(person);
        return genogramService.addGeno(genogram);
    }
}
