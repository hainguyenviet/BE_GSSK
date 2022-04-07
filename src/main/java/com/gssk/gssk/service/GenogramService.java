package com.gssk.gssk.service;

import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.GenogramRepository;
import com.gssk.gssk.repository.RelativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class GenogramService {
    @Autowired
    GenogramRepository genogramNodeRepository;
    public Iterable<Genogram> getAllNodes(){return genogramNodeRepository.findAll();}
    public Genogram FindByID(String id){return genogramNodeRepository.findById(id).get();}

    public Genogram ConnectingDot(Genogram target,List<Relative> rel)
    {
        RelativeService relativeService=new RelativeService(rel);
        Genogram result=target;
        target.setHusband(relativeService.getRelativeByRelation("husband").getId());
        target.setWife(relativeService.getRelativeByRelation("wife").getId());
        target.setM_key(relativeService.getRelativeByRelation("mother").getId());
        target.setF_key(relativeService.getRelativeByRelation("father").getId());
        return result;
    }

    public List<Genogram> ConvertFromPerson(Relative person)
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
    }
    public Genogram updateAttribute(String argv,Genogram target,String attr[])
    {
        Genogram update=target;
        switch (argv)
        {
            //case "add": update.addAttb(attr[0]);
            //case "remove":update.removeAttb(attr[0]);
            //case "replace":update.replaceAttb(attr);
        }
        return update;
    }

}
