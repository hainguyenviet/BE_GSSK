package com.gssk.gssk.service;

import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.GenogramRepository;
import com.gssk.gssk.repository.PersonRepository;
import com.gssk.gssk.repository.RelativeRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class GenogramService {
    @Autowired
    GenogramRepository genogramNodeRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    RelativeRepository relativeRepository;
    private RelativeService relativeService;

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
    public void ConvertPersonToGenogram(String id){
        Genogram genogram = new Genogram();
        Iterable<Relative> relatives = relativeRepository.findAll();
        List<Relative> relativeList = Streamable.of(relatives).toList();
        Person person = personRepository.findById(id).get();
        genogram.setId(person.getId());
        genogram.setName(person.getFirstName() + " " + person.getLastName());
        genogram.setSex(person.getGender());
        if (Objects.equals(genogram.getSex(), "male")){
            if (this.relativeRepository.findByRelationIs("wife").getName() != null){
                genogram.setWife(this.relativeRepository.findByRelationIs("wife").getRid());
            }
        }
        if (this.relativeRepository.findByRelationIs("father") != null){
            genogram.setF_key(this.relativeRepository.findByRelationIs("father").getRid());
        }
        if (this.relativeRepository.findByRelationIs("mother") != null){
            genogram.setM_key(this.relativeRepository.findByRelationIs("mother").getRid());
        }
        genogramNodeRepository.save(genogram);
        /*System.gc();
        for (int i=1; i<=relativeRepository.count();i++){

        }
        for (Relative r: relativeList){
            Genogram genogram = new Genogram();

        }*/
    }
}
