package com.gssk.gssk.service;

import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.GenogramRepository;
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


    public List<Genogram> ConvertFromPerson(Relative person)
    {
        List<Relative> relatives= person.getRelativeList();
        List<Genogram> result=new ArrayList<>();
        Genogram node=new Genogram(person);
        result.add(node);

        for (Relative r:relatives
             ) {
            node=new Genogram(r);
            result.add(node);
        }

        return result;
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
