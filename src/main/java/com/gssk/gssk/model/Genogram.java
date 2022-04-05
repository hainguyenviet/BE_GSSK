package com.gssk.gssk.model;

import com.gssk.gssk.service.RelativeService;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "genogram")
public class Genogram {

    @Column(name = "key")
    private String id;
    @Column(name="name")
    private String name;
    @Column(name="sex")
    private String sex;
    @Column(name="motherKey")
    private String m_key;
    @Column(name="fatherKey")
    private String f_key;
    @Column(name="wife")
    private String wife;
    @Column(name="husband")
    private String husband;
    @Column(name="attributes")
    private List<String> attb ;
    public void addAttb(String target)
    {
        attb.add(target);
    }
    public void removeAttb(String target)
    {
        attb.remove(target);
    }

    public Genogram(Relative person)
    {
        RelativeService relativeService=new RelativeService(person.getRelativeList());
        id=person.getId();
        sex=person.getGender();
        name=person.getFirstName()+" "+person.getLastName();
        husband=relativeService.getRelativeByRelation("husband").getId();
        wife=relativeService.getRelativeByRelation("wife").getId();
        f_key=relativeService.getRelativeByRelation("father").getId();
        m_key=relativeService.getRelativeByRelation("mother").getId();
    }

}