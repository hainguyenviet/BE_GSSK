package com.gssk.gssk.controller;


import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.service.GenogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/genogram")
public class GenogramController {
    @Autowired
    GenogramService genogramService;

    @GetMapping
    public List<Genogram> getAllGenogram(){return (List<Genogram>)genogramService.getAllNodes();}

    @GetMapping(value="/{id}", produces = "application/json")
    public Genogram genogram(@PathVariable("id") String id) {return genogramService.FindByID(id);}

    @PostMapping(value = "convert/{id}", produces = "application/json")
    public void generateGenofromID(@PathVariable("id") String id){
        genogramService.ConvertPersonToGenogram(id);
    }
}
