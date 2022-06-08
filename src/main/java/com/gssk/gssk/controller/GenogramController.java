package com.gssk.gssk.controller;


import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.service.GenogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/genogram")
public class GenogramController {
    @Autowired
    GenogramService genogramService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/all", produces = "application/json")
    public List<Genogram> getAllGenogram(){return (List<Genogram>)genogramService.getAllNodes();}

    //@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping(value="/{key}", produces = "application/json")
    public List<Genogram> genogram(@PathVariable("key") Long key) {return (List<Genogram>)genogramService.getGenogram(key);}

    //@PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping(value = "convert/{key}", produces = "application/json")
    public void generateGenofromID(@PathVariable("key") Long key){
        genogramService.ConvertPersonToGenogram(key);
    }
}
