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

    @GetMapping(value = "/all", produces = "application/json")
    public List<Genogram> getAllGenogram(){return (List<Genogram>)genogramService.getAllNodes();}

    @GetMapping(value="/{key}", produces = "application/json")
    public List<Genogram> genogram(@PathVariable("key") Long key) {return (List<Genogram>)genogramService.getGenogram(key);}

    @PostMapping(value = "convert/{key}", produces = "application/json")
    public void generateGenofromID(@PathVariable("key") Long key){
        genogramService.ConvertPersonToGenogram(key);
    }
}
