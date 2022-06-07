package com.gssk.gssk.service;

import com.gssk.gssk.dto.GenogramDTO;
import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Illness;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.dto.RelativeDTO;
import com.gssk.gssk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


import java.security.SecureRandom;
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
    @Autowired
    HealthRecordRepository healthRecordRepository;
    @Autowired
    IllnessRepository illnessRepository;

    static final String Generator = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public Iterable<Genogram> getAllNodes(){return genogramNodeRepository.findAll();}

    public List<Genogram> getGenogram(Long key){
        List<Genogram> result = genogramNodeRepository.findByKey(key);
        for (Genogram g : result){
            String test = String.join(",", g.getA());
            if (Objects.equals(test, "")){
                g.setA(Collections.emptyList());
            }
        }
        return result;
    }



    public void ConvertPersonToGenogram(Long id){
        List<Genogram> genogramList = new ArrayList<>();


        List<String> attributes = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();


        attributes.add("ME");

        // Truyền thông tin người điền form sang genogram
        Person person = personRepository.findById(id).get();
        List<Relative> relativeList = person.getRelativeList();
        Genogram genogram = new Genogram();
        genogram.setKey(person.getId());
        genogram.setN(person.getFirstName() + " " + person.getLastName());
        List<Illness> illnessList =  person.getHealthRecord().getIllnessList();
        for (Illness i:illnessList
             ) {
            attributes.add(i.getName());
        }



        // SET GENDER
        if (Objects.equals(person.getGender(), "Nam")){
            genogram.setS("M");
        }
        else if (Objects.equals(person.getGender(), "Nữ")){
            genogram.setS("F");
        }
        ///////////////////////////////////////////////
        // SET HUSBAND / WIFE KEY
        if (Objects.equals(genogram.getS(), "M")){
            for (Relative r : relativeList){
                if (Objects.equals(r.getRelation(), "Vợ")){
                    genogram.setUx(r.getRid());
                }
            }
        }
        if (Objects.equals(genogram.getS(), "F")){
            for (Relative r : relativeList){
                if (Objects.equals(r.getRelation(), "Chồng")){
                    genogram.setVir(r.getRid());
                }
            }
        }
        ///////////////////////////////////////////////
        // SET FATHER - MOTHER KEY
        for (Relative r : relativeList){
            if (Objects.equals(r.getRelation(), "Cha")){
                genogram.setF(r.getRid());
            }
        }
        for (Relative r : relativeList){
            if (Objects.equals(r.getRelation(), "Mẹ")){
                genogram.setM(r.getRid());
            }
        }
        //////////////////////////////////////////////
        // SET ATTRIBUTES
        if (Objects.equals(genogram.getS(), "M")){
            genogram.setA(setRelativeAttr(attributes));
        }
        else {
            genogram.setA(setWomanRelativeAttr(attributes));
        }
        int len = 8;
        StringBuilder sb = new StringBuilder(len);
        for (int i=0; i<len; i++){
            sb.append(Generator.charAt(rnd.nextInt(Generator.length())));
        }
        /////////////////////////////////////////////
        // SET CONSTRAINT LIST ID
        genogram.setListID(sb.toString());
        genogramList.add(genogram);


        // CONVERT RELATIVE LIST TO GENOGRAM
        for (Relative r : relativeList){
            RelativeDTO relativeDTO = modelMapper.map(r, RelativeDTO.class);
            GenogramDTO genogramDTO = new GenogramDTO();
            // SET KEY
            genogramDTO.setKey(relativeDTO.getRid());
            // SET NAME
            genogramDTO.setN(relativeDTO.getName());
            List<Illness> illnessRelative = r.getIllnessRelative();
            List<String> relativeAttributes = new ArrayList<>();
            for (Illness i : illnessRelative){
                relativeAttributes.add(i.getName());
            }

            // SET GENDER
            if (Objects.equals(relativeDTO.getGender(), "Nam")){
                genogramDTO.setS("M");
            }
            else if (Objects.equals(relativeDTO.getGender(), "Nữ")){
                genogramDTO.setS("F");
            }
            ///////////////////////////////////////////////////
            // INPUT DEAD
            if( r.getDeath_age()>-1 || Objects.equals(r.getIsDead(), "true"))
                relativeAttributes.add("DEAD");
            // SET ATTRIBUTE
            if (relativeAttributes.isEmpty()){
                genogramDTO.setA(Collections.<String> emptyList());
            } else if (Objects.equals(genogramDTO.getS(), "M")){
                genogramDTO.setA(setRelativeAttr(relativeAttributes));
            } else if (Objects.equals(genogramDTO.getS(), "F")){
                genogramDTO.setA(setWomanRelativeAttr(relativeAttributes));
            }
            // SET CONSTRAINT LIST ID
            genogramDTO.setListID(genogram.getListID());

            // IF RELATIVE IS PERSON'S WIFE
            if (Objects.equals(genogramDTO.getKey(), genogram.getUx())){
                // SET HUSBAND KEY
                genogramDTO.setVir(person.getId());
                for (Relative r1 : relativeList){
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "father in law")) {
                        genogramDTO.setF(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "mother in law")) {
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S FATHER IN LAW
            if (Objects.equals(relativeDTO.getRelation(), "father in law")){
                for (Relative r1 : relativeList){
                    // SET WIFE
                    if (Objects.equals(r1.getRelation(), "mother in law")) {
                        genogramDTO.setUx(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "paternal grandmother in law")) {
                        genogramDTO.setM(r1.getRid());
                    }
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "paternal grandfather in law")) {
                        genogramDTO.setF(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S MOTHER IN LAW
            if (Objects.equals(relativeDTO.getRelation(), "mother in law")){
                for (Relative r1 : relativeList){
                    // SET WIFE
                    if (Objects.equals(r1.getRelation(), "father in law")) {
                        genogramDTO.setVir(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "maternal grandmother in law")) {
                        genogramDTO.setM(r1.getRid());
                    }
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "maternal grandfather in law")) {
                        genogramDTO.setF(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S PATERNAL GRANDFATHER / GRANDMOTHER IN LAW
            if (Objects.equals(relativeDTO.getRelation(), "paternal grandfather in law")){
                for (Relative r1 : relativeList){
                    // SET WIFE
                    if (Objects.equals(r1.getRelation(), "paternal grandmother in law")) {
                        genogramDTO.setUx(r1.getRid());
                    }
                }
            }
            if (Objects.equals(relativeDTO.getRelation(), "paternal grandmother in law")){
                for (Relative r1 : relativeList){
                    // SET HUSBAND
                    if (Objects.equals(r1.getRelation(), "paternal grandfather in law")) {
                        genogramDTO.setVir(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S MATERNAL GRANDFATHER / GRANDMOTHER IN LAW
            if (Objects.equals(relativeDTO.getRelation(), "maternal grandfather in law")){
                for (Relative r1 : relativeList){
                    // SET WIFE
                    if (Objects.equals(r1.getRelation(), "maternal grandmother in law")) {
                        genogramDTO.setUx(r1.getRid());
                    }
                }
            }
            if (Objects.equals(relativeDTO.getRelation(), "maternal grandmother in law")){
                for (Relative r1 : relativeList){
                    // SET HUSBAND
                    if (Objects.equals(r1.getRelation(), "maternal grandfather in law")) {
                        genogramDTO.setVir(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S HUSBAND
            else if (Objects.equals(genogramDTO.getKey(), genogram.getVir())){
                // SET WIFE KEY
                genogramDTO.setUx(person.getId());
                for (Relative r1 : relativeList){
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "father in law")) {
                        genogramDTO.setF(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "mother in law")) {
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }
            ///////////////////////////////////////////////////

            // IF RELATIVE IS PERSON'S FATHER
            if (Objects.equals(genogramDTO.getKey(), genogram.getF())){
                for (Relative r1 : relativeList){
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "Ông nội")){
                        genogramDTO.setF(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "Bà nội")){
                        genogramDTO.setM(r1.getRid());
                    }
                    // SET WIFE KEY
                    if (Objects.equals(r1.getRelation(), "Mẹ")){
                        genogramDTO.setUx(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S MOTHER
            if (Objects.equals(genogramDTO.getKey(), genogram.getM())){
                for (Relative r1 : relativeList){
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "Ông ngoại")){
                        genogramDTO.setF(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "Bà ngoại")){
                        genogramDTO.setM(r1.getRid());
                    }
                    // SET HUSBAND KEY
                    if (Objects.equals(r1.getRelation(), "Cha")){
                        genogramDTO.setVir(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S BROTHER & SISTER
            if (Objects.equals(relativeDTO.getRelation(), "Anh ruột") || Objects.equals(relativeDTO.getRelation(), "Chị ruột") ||
                    Objects.equals(relativeDTO.getRelation(), "Em ruột")){
                for (Relative r1 : relativeList){
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "Cha")){
                        genogramDTO.setF(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "Mẹ")){
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S PATERNAL GRANDFATHER
            if (Objects.equals(relativeDTO.getRelation(), "Ông nội")){
                // SET WIFE KEY
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "Bà nội")){
                        genogramDTO.setUx(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S PATERNAL GRANDMOTHER
            if (Objects.equals(relativeDTO.getRelation(), "Bà nội")){
                // SET HUSBAND KEY
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "Ông nội")){
                        genogramDTO.setVir(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S MATERNAL GRANDFATHER
            if (Objects.equals(relativeDTO.getRelation(), "Ông ngoại")){
                // SET WIFE KEY
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "Bà ngoại")){
                        genogramDTO.setUx(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S MATERNAL GRANDMOTHER
            if (Objects.equals(relativeDTO.getRelation(), "Bà ngoại")){
                // SET HUSBAND KEY
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "Ông ngoại")){
                        genogramDTO.setVir(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS FATHER'S UNCLE OR AUNT
            // uncle 1 = bác trai, uncle 2 = chú, aunt 1 = cô
            if (Objects.equals(relativeDTO.getRelation(), "Bác") || Objects.equals(relativeDTO.getRelation(), "Chú") || Objects.equals(relativeDTO.getRelation(), "Cô")){
                for (Relative r1 : relativeList){
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "Ông nội")){
                        genogramDTO.setF(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "Bà nội")){
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS MOTHER'S UNCLE OR AUNT
            // uncle 3 = cậu, aunt 2 = dì
            if (Objects.equals(relativeDTO.getRelation(), "Cậu") || Objects.equals(relativeDTO.getRelation(), "Dì")){
                for (Relative r1 : relativeList){
                    // SET FATHER KEY
                    if (Objects.equals(r1.getRelation(), "Ông ngoại")){
                        genogramDTO.setF(r1.getRid());
                    }
                    // SET MOTHER KEY
                    if (Objects.equals(r1.getRelation(), "Bà ngoại")){
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }

            // IF RELATIVE IS PERSON'S CHILD
            if (Objects.equals(relativeDTO.getRelation(), "Con ruột")) {
                // IF PERSON IS MALE
                if (Objects.equals(genogram.getS(), "M")) {
                    // SET FATHER KEY
                    genogramDTO.setF(genogram.getKey());
                    // SET MOTHER KEY
                    for (Relative r1 : relativeList){
                        if (Objects.equals(r1.getRelation(), "Vợ")){
                            genogramDTO.setM(r1.getRid());
                        }
                    }
                }

                // IF PERSON IS FEMALE
                if (Objects.equals(genogram.getS(), "F")) {
                    // SET MOTHER KEY
                    genogramDTO.setM(genogram.getKey());
                    // SET FATHER KEY
                    for (Relative r1 : relativeList){
                        if (Objects.equals(r1.getRelation(), "Chồng")){
                            genogramDTO.setF(r1.getRid());
                        }
                    }
                }
            }


            Genogram genogram1 = modelMapper.map(genogramDTO,Genogram.class);
            genogramList.add(genogram1);
        }

        genogramNodeRepository.saveAll(genogramList);
    }

    public List<String> setWomanRelativeAttr(List<String> disease)
    {
        //Dead sẽ là ưu tiên hàng đầu để xét thêm vào attr
        //Lưu ý chỉ add attr của dead vào hàng cuối nhưng đừng lo, cái này sẽ xếp luôn dead vào hàng cuối
        int amount=disease.size();


        List<String> result=new ArrayList<>();
        for (String item:disease
        ) {
            if (Objects.equals(item, "ME"))
            {
                result.add("ME");
                amount--;
            }

            if (item.equals("DEAD"))
            {
                amount--;
            }
        }




        for (String item:disease
        ) {
            if (!item.equals("DEAD")&&!item.equals("ME"))
            {
                if (amount == 1){
                    result.add("1DISEASE");
                }
                else if (amount >1){
                    result.add("NDISEASE");
                    result.add("FDISEASE");
                    break;
                }
                //result.add(disease_amount+item);
            }
        }

        for (String item:disease
        ) {
            if (item.equals("DEAD"))
            {
                result.add(item);
            }
        }

        return result;

    }


    public List<String> setRelativeAttr(List<String>attrList_target)
    {
        //Dead sẽ là ưu tiên hàng đầu để xét thêm vào attr
        //Lưu ý chỉ add attr của dead vào hàng cuối nhưng đừng lo, cái này sẽ xếp luôn dead vào hàng cuối
        int disease_amount=attrList_target.size();


        List<String> result=new ArrayList<>();
            for (String item:attrList_target
            ) {
                if (Objects.equals(item, "ME"))
                {
                    result.add("ME");
                    disease_amount--;
                }

                if (item.equals("DEAD"))
                {
                    disease_amount--;
                }
            }




        for (String item:attrList_target
        ) {
            if (!item.equals("DEAD")&&!item.equals("ME"))
            {
                if (disease_amount == 1){
                    result.add("1DISEASE");
                }
                else if (disease_amount >1){
                    result.add("NDISEASE");
                    break;
                }
                //result.add(disease_amount+item);
            }
        }

        for (String item:attrList_target
        ) {
            if (item.equals("DEAD"))
            {
                result.add(item);
            }
        }

        return result;

    }




}
