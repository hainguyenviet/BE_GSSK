package com.gssk.gssk.model;

import com.gssk.gssk.service.RelativeService;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "genogram")
public class Genogram {

    @Id
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
    public void replaceAttb(String target[])
    {
        attb.set(attb.indexOf(target[0]),target[1]);
    }
    public Genogram(Relative person)
    {
        sex=person.getGender();
        name=person.getFirstName()+" "+person.getLastName();
        HealthRecord HR=person.getHealthRecord();
        for (Illness i:HR.getIllnessList()
             ) {
            attb.add(i.getId());
        }
    }

}