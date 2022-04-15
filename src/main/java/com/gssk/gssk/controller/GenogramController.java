package com.gssk.gssk.controller;


import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.service.GenogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("geno")
public class GenogramController {
    @Autowired
    GenogramService genogramService;

    @GetMapping("/{cv}")
    public List<Genogram> ConvertFromPerson(@PathVariable("cv") Person person) { return genogramService.ConvertFromPerson(person); }

    @GetMapping
    public List<Genogram> getAllGenogram(){return (List<Genogram>)genogramService.getAllNodes();}

    @GetMapping(value="/{id}", produces = "application/json")
    public Genogram genogram(@PathVariable("id") String id) {return genogramService.FindByID(id);}
}
