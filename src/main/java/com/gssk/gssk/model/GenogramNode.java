package com.gssk.gssk.model;

import com.gssk.gssk.repository.RelativeRepository;
import com.gssk.gssk.service.RelativeService;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "genogram")
public class GenogramNode {

    //key, the unique ID of the person
    //n, the person's name
    //s, the person's sex
    //m, the person's mother's key
    //f, the person's father's key
    //ux, the person's wife
    //vir, the person's husband
    //a, an Array of the attributes or markers that the person has
    @Column(name = "key")
    private String key;
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

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public String getM_key() {
        return m_key;
    }

    public String getF_key() {
        return f_key;
    }

    public String getWife() {
        return wife;
    }

    public String getHusband() {
        return husband;
    }


    public List<String> getAttb() {
        return attb;
    }

    public GenogramNode(Person p)
    {

        key=p.getId();
        name=p.getFirstName()+" "+p.getLastName();
        sex=p.getGender();
        List<Relative> rl=p.getRelativeList();
        RelativeService processing=new RelativeService(rl);
        wife=processing.getRelativeByRelation("wife").getId();
        husband=processing.getRelativeByRelation("husband").getId();
        f_key=processing.getRelativeByRelation("father").getId();
        m_key=processing.getRelativeByRelation("mother").getId();
        HealthRecord h=p.getHealthRecord();
        List<Illness> Il=h.getIllnessList();

        for (Illness i:Il)
        {
            attb.add(i.getId());
        }



    }
    public  GenogramNode(Relative r)
    {
        key=r.getId();
        name=r.getFirstName()+" "+r.getLastName();
        sex=r.getGender();
        List<Relative> rl=r.getRelativeList();
        RelativeService processing=new RelativeService(rl);
        wife=processing.getRelativeByRelation("wife").getId();
        husband=processing.getRelativeByRelation("husband").getId();
        f_key=processing.getRelativeByRelation("father").getId();
        m_key=processing.getRelativeByRelation("mother").getId();
        HealthRecord h=r.getHealthRecord();
        List<Illness> Il=h.getIllnessList();

        for (Illness i:Il)
        {
            attb.add(i.getId());
        }

    }




}
