package com.gssk.gssk.model;

import com.gssk.gssk.repository.RelativeRepository;
import com.gssk.gssk.service.RelativeService;
import lombok.Data;

import java.util.List;

@Data
public class GenogramNode {

    //key, the unique ID of the person
    //n, the person's name
    //s, the person's sex
    //m, the person's mother's key
    //f, the person's father's key
    //ux, the person's wife
    //vir, the person's husband
    //a, an Array of the attributes or markers that the person has

    private String key;
    private String n;
    private String s;
    private String m;
    private String f;
    private String ux;
    private String vir;

    public String getKey() {
        return key;
    }

    public String getN() {
        return n;
    }

    public String getS() {
        return s;
    }

    public String getM() {
        return m;
    }

    public String getF() {
        return f;
    }

    public String getUx() {
        return ux;
    }

    public String getVir() {
        return vir;
    }

    public GenogramNode(Person p)
    {
        key=p.getId();
        n=p.getFirstName()+" "+p.getLastName();
        s=p.getGender();
        List<Relative> rl=p.getRelativeList();
        RelativeService processing=new RelativeService(rl);
        ux=processing.getRelativeByRelation("wife").getId();
        vir=processing.getRelativeByRelation("husband").getId();
        f=processing.getRelativeByRelation("father").getId();
        m=processing.getRelativeByRelation("mother").getId();


    }

}
