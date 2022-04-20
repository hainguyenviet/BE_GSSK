package com.gssk.gssk.service;

import com.gssk.gssk.dto.GenogramDTO;
import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Illness;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.dto.PersonDTO;
import com.gssk.gssk.dto.RelativeDTO;
import com.gssk.gssk.repository.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


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

    public Iterable<Genogram> getAllNodes(){return genogramNodeRepository.findAll();}
    public Genogram FindByID(String id){return genogramNodeRepository.findById(id).get();}


    public void ConvertPersonToGenogram(String id){
        List<Genogram> genogramList = new ArrayList<>();
        Iterable<Relative> relatives = relativeRepository.findAll();
        List<Relative> relativeList = Streamable.of(relatives).toList();
        Iterable<Illness> illnesses = illnessRepository.findAll();
        List<Illness> illnessList = Streamable.of(illnesses).toList();
        List<String> attributes = new ArrayList<>();

        for (Illness i:illnessList){
            attributes.add(i.getName());
        }



        // Truyền thông tin người điền form sang genogram
        Person person = personRepository.findById(id).get();
        Genogram genogram = new Genogram();
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
        genogram.setAttb(attributes);
        genogramList.add(genogram);

        // Truyền thông tin thân nhân sang genogram
        for (Relative r : relativeList){
            ModelMapper modelMapper = new ModelMapper();
            RelativeDTO relativeDTO = modelMapper.map(r, RelativeDTO.class);
            GenogramDTO genogramDTO = new GenogramDTO();
            genogramDTO.setId(relativeDTO.getRid());
            genogramDTO.setName(relativeDTO.getName());
            genogramDTO.setSex(relativeDTO.getGender());
            genogramDTO.setAttb(relativeDTO.getIllnessName());

            // Nếu thân nhân có mối quan hệ là cha
            if (Objects.equals(genogramDTO.getId(), genogram.getF_key())){
                // Set khóa cha, mẹ
                if (relativeRepository.findByRelationIs("paternal grandfather") != null ){
                    genogramDTO.setF_key(this.relativeRepository.findByRelationIs("paternal grandfather").getRid());
                }
                if (relativeRepository.findByRelationIs("paternal grandmother") != null ){
                    genogramDTO.setM_key(this.relativeRepository.findByRelationIs("paternal grandmother").getRid());
                }
                // Set khóa vợ
                genogramDTO.setWife(relativeRepository.findByRelationIs("mother").getRid());
            }

            // Nếu thân nhân có mối quan hệ là mẹ
            if (Objects.equals(genogramDTO.getId(), genogram.getM_key())){
                // Set khóa cha, mẹ
                if (relativeRepository.findByRelationIs("maternal grandfather") != null ){
                    genogramDTO.setF_key(this.relativeRepository.findByRelationIs("maternal grandfather").getRid());
                }
                if (relativeRepository.findByRelationIs("maternal grandmother") != null ){
                    genogramDTO.setM_key(this.relativeRepository.findByRelationIs("maternal grandmother").getRid());
                }
                // Set khóa chồng
                genogramDTO.setHusband(relativeRepository.findByRelationIs("father").getRid());
            }

            // Nếu thân nhân có mối quan hệ là anh, em trai ruột
            if (Objects.equals(relativeDTO.getRelation(), "brother")){
                if (this.relativeRepository.findByRelationIs("father") != null){
                    genogramDTO.setF_key(this.relativeRepository.findByRelationIs("father").getRid());
                }
                if (this.relativeRepository.findByRelationIs("mother") != null){
                    genogramDTO.setM_key(this.relativeRepository.findByRelationIs("mother").getRid());
                }
            }

            // Nếu thân nhân có mối quan hệ là chị, em gái ruột
            if (Objects.equals(relativeDTO.getRelation(), "sister")){
                if (this.relativeRepository.findByRelationIs("father") != null){
                    genogramDTO.setF_key(this.relativeRepository.findByRelationIs("father").getRid());
                }
                if (this.relativeRepository.findByRelationIs("mother") != null){
                    genogramDTO.setM_key(this.relativeRepository.findByRelationIs("mother").getRid());
                }
            }

            // Nếu thân nhân có mối quan hệ là ông nội
            if (Objects.equals(relativeDTO.getRelation(), "paternal grandfather")){
                // Set vợ
                genogramDTO.setWife(this.relativeRepository.findByRelationIs("paternal grandmother").getRid());
            }

            // Nếu thân nhân có mối quan hệ là bà nội
            if (Objects.equals(relativeDTO.getRelation(), "paternal grandmother")){
                // Set vợ
                genogramDTO.setHusband(this.relativeRepository.findByRelationIs("paternal grandfather").getRid());
            }

            // Nếu thân nhân có mối quan hệ là ông ngoại
            if (Objects.equals(relativeDTO.getRelation(), "maternal grandfather")){
                // Set vợ
                genogramDTO.setWife(this.relativeRepository.findByRelationIs("maternal grandmother").getRid());
            }

            // Nếu thân nhân có mối quan hệ là bà ngoại
            if (Objects.equals(relativeDTO.getRelation(), "maternal grandmother")){
                // Set vợ
                genogramDTO.setHusband(this.relativeRepository.findByRelationIs("maternal grandfather").getRid());
            }

            // Nếu thân nhân có mối quan hệ là anh, chị em ruột của cha
            // uncle 1 = bác trai, uncle 2 = chú, aunt 1 = cô
            if (Objects.equals(relativeDTO.getRelation(), "uncle 1") || Objects.equals(relativeDTO.getRelation(), "uncle 2") || Objects.equals(relativeDTO.getRelation(), "aunt 1")){
                // Set cha, mẹ
                if (relativeRepository.findByRelationIs("paternal grandfather") != null ){
                    genogramDTO.setF_key(this.relativeRepository.findByRelationIs("paternal grandfather").getRid());
                }
                if (relativeRepository.findByRelationIs("paternal grandmother") != null ){
                    genogramDTO.setM_key(this.relativeRepository.findByRelationIs("paternal grandmother").getRid());
                }
            }

            // Nếu thân nhân có môí quan hệ là anh, chị em ruột của mẹ
            // uncle 3 = cậu, aunt 2 = dì
            if (Objects.equals(relativeDTO.getRelation(), "uncle 3") || Objects.equals(relativeDTO.getRelation(), "aunt 2")){
                // Set cha, mẹ
                if (relativeRepository.findByRelationIs("maternal grandfather") != null ){
                    genogramDTO.setF_key(this.relativeRepository.findByRelationIs("maternal grandfather").getRid());
                }
                if (relativeRepository.findByRelationIs("maternal grandmother") != null ){
                    genogramDTO.setM_key(this.relativeRepository.findByRelationIs("maternal grandmother").getRid());
                }
            }

            // Nếu người điền form là nam
            if (Objects.equals(genogram.getSex(), "male")){
                // Nếu thân nhân có mối quan hệ con cái
                if (Objects.equals(relativeDTO.getRelation(), "child")) {
                    // Set khóa cha, mẹ
                    genogramDTO.setF_key(genogram.getId());
                    genogramDTO.setM_key(relativeRepository.findByRelationIs("wife").getRid());
                }

            }

            // Nếu người điền form là nữ
            if (Objects.equals(genogram.getSex(), "female")){
                // Nếu thân nhân có mối quan hệ con cái
                if (Objects.equals(relativeDTO.getRelation(), "child")) {
                    // Set khóa cha, mẹ
                    genogramDTO.setM_key(genogram.getId());
                    genogramDTO.setF_key(relativeRepository.findByRelationIs("husband").getRid());
                }

                //

            }

            Genogram genogram1 = modelMapper.map(genogramDTO,Genogram.class);
            genogramList.add(genogram1);
        }

        genogramNodeRepository.saveAll(genogramList);
    }
}
