package com.gssk.gssk.service;

import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.GenogramRepository;
import com.gssk.gssk.repository.PersonRepository;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class GenogramService {
    @Autowired
    GenogramRepository genogramNodeRepository;
    @Autowired
    PersonRepository personRepository;

    public Iterable<Genogram> getAllNodes(){return genogramNodeRepository.findAll();}
    public Genogram FindByID(String id){return genogramNodeRepository.findById(id).get();}

    /*public Genogram ConnectingDot(Genogram target,List<Relative> rel)
    {
        RelativeService relativeService=new RelativeService(rel);
        Genogram result=target;
        target.setHusband(relativeService.getRelativeByRelation("husband").getId());
        target.setWife(relativeService.getRelativeByRelation("wife").getId());
        target.setM_key(relativeService.getRelativeByRelation("mother").getId());
        target.setF_key(relativeService.getRelativeByRelation("father").getId());
        return result;
    }

    public List<Genogram> ConvertFromPerson(Person person)
    {
        List<Relative> relatives= person.getRelativeList();
        List<Genogram> GenogramList=new ArrayList<>();
        Genogram node=new Genogram(person);
        Genogram nodeAdd;

        nodeAdd=ConnectingDot(node,person.getRelativeList());
        GenogramList.add(nodeAdd);
        for (Relative r:relatives
             ) {
            node=new Genogram(r);
            nodeAdd=ConnectingDot(node,r.getRelativeList());
            GenogramList.add(nodeAdd);
        }



        return GenogramList;
    }*/




    @SneakyThrows
    public Genogram ConvertPersonToGenogram(String id){
        Genogram genogram = new Genogram();
        Person person = personRepository.findById(id).get();
        genogram.setId(person.getId());
        genogram.setName(person.getFirstName() + " " + person.getLastName());
        genogram.setSex(person.getGender());
        return genogramNodeRepository.save(genogram);
    }

}
