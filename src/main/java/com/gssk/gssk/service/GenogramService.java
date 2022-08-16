package com.gssk.gssk.service;

import com.gssk.gssk.dto.GenogramDTO;
import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Illness;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.dto.RelativeDTO;
import com.gssk.gssk.repository.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


import java.security.SecureRandom;
import java.util.*;

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

    public List<String> risk(String username){
        List<Integer> Mark = new ArrayList<>();//đánh dấu cho Trực hệ 2
        boolean flag;//đánh dấu sự kiện

        List<String> result = new ArrayList<>();
        // lưu trực hệ 1 bệnh
        List<String> direct1 = new ArrayList<>();
        // lưu trực hệ 2 bệnh
        List<String> direct2 = new ArrayList<>();
        //lưu trực hệ 3 bệnh
        //Nội
        int cPaternal=0;
        //Ngoại
        int cMaternal=0;

        // Kiểm tra relation thuộc trực hệ 1
        List<String> checkDirect1 = new ArrayList<>(Arrays.asList("Cha", "Mẹ", "Anh ruột", "Em ruột", "Chị ruột", "Con ruột"));
        // Kiểm tra relation thuộc trực hệ 2
        List<String> checkDirect2 = new ArrayList<>(Arrays.asList("Ông nội", "Bà nội", "Ông ngoại", "Bà ngoại", "Cô", "Dì", "Chú",
                "Cậu"));
        // Kiểm tra relation thuộc trực hệ 3
        List<String> checkDirect3 = new ArrayList<>(Arrays.asList("Anh trai họ", "Em trai họ", "Em gái họ", "Chị gái họ"));
        // bên phả hệ nội
        List<String> paternalSide = new ArrayList<>(Arrays.asList("Ông nội", "Bà nội", "Cha", "Cô", "Chú"));
        // bên phả hệ ngoại
        List<String> maternalSide = new ArrayList<>(Arrays.asList("Ông ngoại", "Bà ngoại", "Mẹ", "Dì","Cậu"));
        List<Integer> direct1Age = new ArrayList<>();
        List<Integer> direct2Age = new ArrayList<>();
        Person person = personRepository.findByUsername(username);
        List<Relative> relativeList = person.getRelativeList();

        for (Relative r: relativeList){
            List<Illness> illnessRelative = r.getIllnessRelative();
            for (Illness i : illnessRelative){
                if (Objects.equals(i.getIllName(),"Ung thư vú")){
                    if (checkDirect1.contains(r.getRelation())){
                        direct1.add(r.getRelation());
                        direct1Age.add((i.getAge_detected()));

                        if (paternalSide.contains(r.getRelation())){
                            cPaternal = cPaternal + 1;
                        } else if (maternalSide.contains(r.getRelation())) {
                            cMaternal = cMaternal + 1;
                        }
                    }
                    else if(checkDirect2.contains(r.getRelation())){



                        direct2.add(r.getRelation());
                        direct2Age.add(i.getAge_detected());
                        if (paternalSide.contains(r.getRelation())){

                            cPaternal = cPaternal + 1;
                        } else if (maternalSide.contains(r.getRelation())) {
                            cMaternal = cMaternal + 1;
                        }




                    }
                    else if(checkDirect3.contains(r.getRelation()))
                    {

                        if (paternalSide.contains(relativeRepository.findByName(r.getParentName()).getRelation())) {
                            cPaternal += 1;
                        }
                        if (maternalSide.contains(relativeRepository.findByName(r.getParentName()).getRelation()))
                            cMaternal+=1;

                    }
                }
            }
        }


        //Xét Trực hệ 3 trước vì Trực hệ 3 chỉ cần là số lượng

        if (cMaternal >= 3 || cPaternal >= 3){
            result.add("UNGTHUVU_CAO");
        }
            else

            //Trực hệ 1 >= 2
            if (direct1.size() >= 2) {
                result.add("UNGTHUVU_CAO");
            }
            //Trực hệ 1 = 1
            else if (direct1.size() == 1) {
                // Khởi phát sớm (<50 tuổi)
                if (direct1Age.stream().anyMatch(integer -> integer > 0) && direct1Age.stream().anyMatch(integer -> integer < 50)) {
                    result.add("UNGTHUVU_CAO");
                }
                // Khởi phát muộn (>=50 tuổi)
                else {

                    // Nếu trực hệ 2 = 0 (chưa xong)
                    if (direct2.size() == 0) {
                        result.add("UNGTHUVU_TB");
                    }
                    // Nếu trực hệ 2 = 1 (Tuấn Anh)
                    if (direct2.size() == 1){


                        // Nếu trực hệ 1 là Cha/Mẹ
                        if (direct1.stream().anyMatch(String -> Objects.equals(String, "Cha")) || direct1.stream().anyMatch(String -> Objects.equals(String, "Mẹ"))) {
                            //Nếu trực hệ 2 cùng bên với trực hệ 1
                            if ((direct1.stream().anyMatch(String -> Objects.equals(String, "Cha")) && direct2.stream().anyMatch(paternalSide::contains)) || (direct1.stream().anyMatch(String -> Objects.equals(String, "Mẹ")) && direct2.stream().anyMatch(maternalSide::contains))) {
                                // Nếu trực hệ 2 < 50 tuổi
                                if (direct2Age.stream().anyMatch(integer -> integer > 0) && direct2Age.stream().anyMatch(integer -> integer < 50)) {
                                    result.add("UNGTHUVU_CAO");
                                }
                                // Nếu trực hệ 2 >= 50 tuổi
                                else {
                                    result.add("UNGTHUVU_THAP");
                                }
                            }
                            // Nếu ko cùng bên trực hệ 1
                            else{
                                result.add("UNGTHUVU_THAP");
                            }
                        }
                        // ko phải cha/mẹ
                        else {
                            result.add("UNGTHUVU_THAP");
                        }
                    }


                    // Nếu trực hệ 2 > 1 (Chương)

                    if (direct2.size() > 1) {
                        flag = false;


                        if (Objects.equals(direct1.get(0), "Mẹ")) {
                            cMaternal = 0;

                            for (String check : direct2) {
                                if (maternalSide.contains(check)) {
                                    cMaternal++;
                                    Mark.add(direct2Age.get(direct2.indexOf(check)));
                                }
                            }
                            if (cMaternal == 1) {
                                flag = true;
                                if (Mark.get(0) < 50)
                                    result.add("UNGTHUVU_CAO");
                                else
                                    result.add("UNGTHUVU_TB");
                            }
                            if (cMaternal == 0) {
                                result.add("UNGTHUVU_TB");
                            }
                        }

                        if (Objects.equals(direct1.get(0), "Cha")) {
                            cPaternal = 0;

                            for (String check : direct2) {
                                if (paternalSide.contains(check)) {
                                    cPaternal++;
                                    Mark.add(direct2Age.get(direct2.indexOf(check)));
                                }
                            }
                            if (cPaternal == 1) {
                                flag = true;
                                if (Mark.get(0) < 50)
                                    result.add("UNGTHUVU_CAO");
                                else
                                    result.add("UNGTHUVU_TB");
                            }
                            if (cPaternal == 0) {
                                result.add("UNGTHUVU_TB");

                            }
                        }

                            if(!flag) {
                                //xét bên nội
                                cPaternal = 0;
                                for (String check : direct2) {
                                    if (paternalSide.contains(check)) {
                                        cPaternal++;
                                        Mark.add(direct2Age.get(direct2.indexOf(check)));
                                    }
                                }
                                if (cPaternal == 2) {
                                    if (Mark.stream().anyMatch(integer -> integer > 0) && Mark.stream().anyMatch(integer -> integer < 50)) {
                                        result.add("UNGTHUVU_CAO");
                                    } else
                                        result.add("UNGTHUVU_TB");
                                }


                                //xét bên ngoại
                                cMaternal = 0;
                                for (String check : direct2) {
                                    if (maternalSide.contains(check)) {
                                        cMaternal++;
                                        Mark.add(direct2Age.get(direct2.indexOf(check)));
                                    }
                                }

                                if (cMaternal == 2) {
                                    if (Mark.stream().anyMatch(integer -> integer > 0) && Mark.stream().anyMatch(integer -> integer < 50)) {
                                        result.add("UNGTHUVU_CAO");
                                    } else
                                        result.add("UNGTHUVU_TB");
                                    }

                                if (cPaternal<2 && cMaternal<2)
                                {
                                    result.add("UNGTHUVU_TB");
                                }



                                }
                            }


                    }


                }



            // Trực hệ 1 = 0
            else {
                // Trực hệ 2 = 0
                if (direct2.size() == 0 || direct2.size() == 1) {
                    result.add("UNGTHUVU_THAP");
                }


                // Trực hệ 2 = 2
                if (direct2.size() == 2) {
                    if (paternalSide.containsAll(direct2) || maternalSide.containsAll(direct2)) {
                        if (direct2Age.stream().anyMatch(integer -> integer > 0) && direct2Age.stream().anyMatch(integer -> integer < 50)) {
                            result.add("UNGTHUVU_CAO");
                        } else {
                            result.add("UNGTHUVU_TB");
                        }
                    }
                    else
                        result.add("UNGTHUVU_THAP");
                }



                //WIP
                if (direct2.size() >=3) {
                    //xét bên nội
                    cPaternal = 0;
                    for (String check : direct2) {
                        if (paternalSide.contains(check)) {
                            cPaternal++;
                            Mark.add(direct2Age.get(direct2.indexOf(check)));
                        }
                    }
                if (cPaternal>=2)
                {
                    if(Mark.stream().anyMatch(integer -> integer > 0)&&Mark.stream().anyMatch(integer -> integer < 50))
                    {
                        result.add("UNGTHUVU_CAO");
                    }
                    else
                    {
                        result.add("UNGTHUVU_TB");
                    }
                }


                //xét bên ngoại
                    cMaternal=0;
                for(String check: direct2)
                {
                    if (maternalSide.contains(check))
                    {
                        cMaternal++;
                        Mark.add(direct2Age.get(direct2.indexOf(check)));
                    }
                }

                if (cMaternal>=2)
                {
                    if(Mark.stream().anyMatch(integer -> integer > 0)&&Mark.stream().anyMatch(integer -> integer < 50))
                    {
                        result.add("UNGTHUVU_CAO");
                    }
                    else
                    {
                        result.add("UNGTHUVU_TB");
                    }
                }

                    if (cPaternal<2 && cMaternal<2)
                    {
                        result.add("UNGTHUVU_THAP");
                    }


                }

            }



        return result;
    }



    public void ConvertPersonToGenogram(String username){
        List<Genogram> genogramList = new ArrayList<>();
        String newLine = System.getProperty("line.separator");

        List<String> attributes = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();



        attributes.add("ME");

        // Truyền thông tin người điền form sang genogram
        Person person = personRepository.findByUsername(username);
        List<Relative> relativeList = person.getRelativeList();
        Genogram genogram = new Genogram();
        genogram.setKey(person.getId());
        List<Illness> illnessList =  person.getHealthRecord().getIllnessList();
        List<String> illnessName = new ArrayList<>();
        for (Illness i:illnessList
             ) {
            if (!i.getName().isEmpty()){
                attributes.add(i.getName());
            }
            if (!i.getIllName().isEmpty()){
                illnessName.add(i.getIllName());
            }
        }
        if(illnessName.isEmpty()){
            genogram.setN(person.getFirstName() + " " + person.getLastName());
        }
        else {
            String display = String.join(", ", illnessName);
            String fullName = person.getFirstName() + " " + person.getLastName();
            String setN = fullName + newLine + display;
            genogram.setN(setN);
        }

        //////////////////////////////////////////////////////////
        /// LƯỢNG GIÁ NGUY CƠ ///////////////////////////////////
        ////////////////////////////////////////////////////////




        /////
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
            List<Illness> illnessRelative = r.getIllnessRelative();
            List<String> relativeAttributes = new ArrayList<>();
            List<String> illnessNameRelative = new ArrayList<>();
            for (Illness i : illnessRelative){
                if (!i.getName().isEmpty()){
                    relativeAttributes.add(i.getName());
                }
                if (!i.getIllName().isEmpty()){
                    illnessNameRelative.add(i.getIllName());
                }
            }
            if (illnessNameRelative.isEmpty()){
                genogramDTO.setN(relativeDTO.getName());
            }
            else {
                String displayR = String.join(", ", illnessNameRelative);
                genogramDTO.setN(relativeDTO.getName()
                        + newLine + displayR);
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

            //IF RELATIVE IS CHÁU
            if (Objects.equals(relativeDTO.getRelation(), "Cháu")){
                for (Relative r1 : relativeList){
                    if(Objects.equals(r1.getName(), relativeDTO.getParentName()) && (Objects.equals(r1.getRelation(), "Anh ruột") || Objects.equals(r1.getRelation(), "Em ruột") || Objects.equals(r1.getRelation(), "Chị ruột"))){
                        if (Objects.equals(r1.getGender(), "Nam")){
                            genogramDTO.setF(r1.getRid());
                        }
                        else{
                            genogramDTO.setM(r1.getRid());
                        }
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

                if (item.equals("HIGH_BC")){
                    result.add("HIGH_BC");
                    disease_amount--;
                }

                if (item.equals("AVERAGE_BC")){
                    result.add("AVERAGE_BC");
                    disease_amount--;
                }

                if (item.equals("LOW_BC")){
                    result.add("LOW_BC");
                    disease_amount--;
                }
            }




        for (String item:attrList_target
        ) {
            if (!item.equals("DEAD")&&!item.equals("ME") && !item.equals("HIGH_BC") && !item.equals("AVERAGE_BC") && !item.equals("LOW_BC") )
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
