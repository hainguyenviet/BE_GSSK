package com.gssk.gssk.service;

import com.gssk.gssk.dto.GenogramDTO;
import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.dto.PersonDTO;
import com.gssk.gssk.dto.RelativeDTO;
import com.gssk.gssk.repository.GenogramRepository;
import com.gssk.gssk.repository.PersonRepository;
import com.gssk.gssk.repository.RelativeRepository;
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

    public Iterable<Genogram> getAllNodes(){return genogramNodeRepository.findAll();}
    public Genogram FindByID(String id){return genogramNodeRepository.findById(id).get();}


    public void ConvertPersonToGenogram(String id){
        List<Genogram> genogramList = new ArrayList<>();
        Iterable<Relative> relatives = relativeRepository.findAll();
        List<Relative> relativeList = Streamable.of(relatives).toList();

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
        genogramList.add(genogram);

        // Truyền thông tin thân nhân sang genogram
        for (Relative r : relativeList){
            ModelMapper modelMapper = new ModelMapper();
            RelativeDTO relativeDTO = modelMapper.map(r, RelativeDTO.class);
            GenogramDTO genogramDTO = new GenogramDTO();
            genogramDTO.setId(relativeDTO.getRid());
            genogramDTO.setName(relativeDTO.getName());
            genogramDTO.setSex(relativeDTO.getGender());

            // Nếu người điền form là nam
            if (Objects.equals(genogram.getSex(), "male")){
                // Set các khóa quan hệ nếu thân nhân là nam
                if (Objects.equals(genogramDTO.getSex(), "male")) {
                    // Nếu thân nhân có mối quan hệ con cái
                    if (Objects.equals(relativeDTO.getRelation(), "child")) {
                        // Set khóa cha, mẹ
                        genogramDTO.setF_key(genogram.getId());
                        genogramDTO.setM_key(relativeRepository.findByRelationIs("wife").getRid());

                        //Set khóa vợ
                        if (relativeRepository.findByRelationIs("daughter in law") != null) {
                            genogramDTO.setWife(relativeRepository.findByRelationIs("daughter in law").getRid());
                        }
                    }
                    // Nếu thân nhân có mối quan hệ là cha
                    if (Objects.equals(genogramDTO.getId(), genogram.getF_key())){
                        // Set khóa cha, mẹ
                        if (relativeRepository.findByRelationIs("grandfather") != null ){
                            genogramDTO.setF_key(relativeRepository.findByRelationIs("grandfather").getRid());
                        }
                        if (relativeRepository.findByRelationIs("grandmother") != null ){
                            genogramDTO.setM_key(relativeRepository.findByRelationIs("grandmother").getRid());
                        }
                        // Set khóa vợ
                        genogramDTO.setWife(relativeRepository.findByRelationIs("mother").getRid());
                    }
                }
                // Set các khóa quan hệ nếu thân nhân là nữ
                if (Objects.equals(genogramDTO.getSex(), "female")){
                    // Nếu thân nhân có mối quan hệ con cái
                    if (Objects.equals(relativeDTO.getRelation(), "child")) {
                        // Set khóa cha, mẹ
                        genogramDTO.setF_key(genogram.getId());
                        genogramDTO.setM_key(relativeRepository.findByRelationIs("wife").getRid());

                        //Set khóa chồng
                        if (relativeRepository.findByRelationIs("son in law") != null) {
                            genogramDTO.setHusband(relativeRepository.findByRelationIs("son in law").getRid());
                        }

                    }
                }
            }

            Genogram genogram1 = modelMapper.map(genogramDTO,Genogram.class);
            genogramList.add(genogram1);
        }

        genogramNodeRepository.saveAll(genogramList);
    }
}
