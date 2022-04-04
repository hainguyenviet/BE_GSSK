package com.gssk.gssk.service;

import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.GenogramNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Service
public class GenogramService {
    @Autowired
    GenogramNodeRepository genogramNodeRepository;
    public Iterable<Genogram> getAllNodes(){return genogramNodeRepository.findAll();}
    public Genogram FindByID(String id){return genogramNodeRepository.findById(id).get();}

    public Genogram ConvertOne(Relative one)
    {
        Genogram result=new Genogram();
        RelativeService relativeService=new RelativeService(one.getRelativeList());
        result.setId(one.getId());
        result.setSex(one.getGender());
        result.setName(one.getFirstName()+" "+one.getLastName());
        result.setHusband(relativeService.getRelativeByRelation("husband").getId());
        result.setWife(relativeService.getRelativeByRelation("wife").getId());
        result.setF_key(relativeService.getRelativeByRelation("father").getId());
        result.setM_key(relativeService.getRelativeByRelation("mother").getId());


        return result;
    }
    public List<Genogram> ConvertFromPerson(Relative person)
    {
        List<Relative> relatives= person.getRelativeList();
        List<Genogram> result=new ArrayList<>();
        result.add(ConvertOne(person));

        for (Relative r:relatives
             ) {
            result.add(ConvertOne(r));
        }

        return result;
    }
    public Genogram updateAttribute(String argv,String attr,Genogram target)
    {
        Genogram update=target;
        switch (argv)
        {
            //case "add": update.addAttb(attb);
            //case "remove":update.removeAttb(attb);
        }
        return update;
    }

}
