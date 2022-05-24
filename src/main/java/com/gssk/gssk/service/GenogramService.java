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
    //public Genogram FindByID(String id){return genogramNodeRepository.findById(id).get();}
    public List<Genogram> getGenogram(String key){return genogramNodeRepository.findByKey(key);}


    public void ConvertPersonToGenogram(String id){
        List<Genogram> genogramList = new ArrayList<>();
        //Iterable<Relative> relatives = relativeRepository.findAll();
        //List<Relative> relativeList = Streamable.of(relatives).toList();
        Iterable<Illness> illnesses = illnessRepository.findAll();
        List<Illness> illnessList = Streamable.of(illnesses).toList();
        List<String> attributes = new ArrayList<>();

        for (Illness i:illnessList){
            attributes.add(i.getName());
        }



        // Truyền thông tin người điền form sang genogram
        Person person = personRepository.findById(id).get();
        List<Relative> relativeList = person.getRelativeList();
        Genogram genogram = new Genogram();
        genogram.setKey(person.getId());
        genogram.setName(person.getFirstName() + " " + person.getLastName());
        genogram.setSex(person.getGender());
        if (Objects.equals(genogram.getSex(), "male")){
            for (Relative r : relativeList){
                if (Objects.equals(r.getRelation(), "wife")){
                    genogram.setWife(r.getRid());
                }
            }
        }
        for (Relative r : relativeList){
            if (Objects.equals(r.getRelation(), "father")){
                genogram.setF_key(r.getRid());
            }
        }
        for (Relative r : relativeList){
            if (Objects.equals(r.getRelation(), "mother")){
                genogram.setM_key(r.getRid());
            }
        }
        genogram.setAttb(attributes);
        int len = 8;
        StringBuilder sb = new StringBuilder(len);
        for (int i=0; i<len; i++){
            sb.append(Generator.charAt(rnd.nextInt(Generator.length())));
        }
        genogram.setListID(sb.toString());
        genogramList.add(genogram);

        // Truyền thông tin thân nhân sang genogram
        for (Relative r : relativeList){
            ModelMapper modelMapper = new ModelMapper();
            RelativeDTO relativeDTO = modelMapper.map(r, RelativeDTO.class);
            GenogramDTO genogramDTO = new GenogramDTO();
            genogramDTO.setKey(relativeDTO.getRid());
            genogramDTO.setName(relativeDTO.getName());
            genogramDTO.setSex(relativeDTO.getGender());
            genogramDTO.setAttb(relativeDTO.getIllnessName());
            genogramDTO.setListID(genogram.getListID());

            // Nếu thân nhân có mối quan hệ là cha
            if (Objects.equals(genogramDTO.getKey(), genogram.getF_key())){
                // Set khóa cha, mẹ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "paternal grandfather")){
                        genogramDTO.setF_key(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "paternal grandmother")){
                        genogramDTO.setM_key(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "mother")){
                        genogramDTO.setWife(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là mẹ
            if (Objects.equals(genogramDTO.getKey(), genogram.getM_key())){
                // Set khóa cha, mẹ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "maternal grandfather")){
                        genogramDTO.setF_key(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "maternal grandmother")){
                        genogramDTO.setM_key(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "father")){
                        genogramDTO.setHusband(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là anh, em trai ruột
            if (Objects.equals(relativeDTO.getRelation(), "brother")){
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "father")){
                        genogramDTO.setF_key(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "mother")){
                        genogramDTO.setM_key(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là chị, em gái ruột
            if (Objects.equals(relativeDTO.getRelation(), "sister")){
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "father")){
                        genogramDTO.setF_key(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "mother")){
                        genogramDTO.setM_key(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là ông nội
            if (Objects.equals(relativeDTO.getRelation(), "paternal grandfather")){
                // Set vợ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "paternal grandmother")){
                        genogramDTO.setWife(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là bà nội
            if (Objects.equals(relativeDTO.getRelation(), "paternal grandmother")){
                // Set chồng
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "paternal grandfather")){
                        genogramDTO.setHusband(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là ông ngoại
            if (Objects.equals(relativeDTO.getRelation(), "maternal grandfather")){
                // Set vợ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "maternal grandmother")){
                        genogramDTO.setWife(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là bà ngoại
            if (Objects.equals(relativeDTO.getRelation(), "maternal grandmother")){
                // Set vợ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "maternal grandfather")){
                        genogramDTO.setHusband(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là anh, chị em ruột của cha
            // uncle 1 = bác trai, uncle 2 = chú, aunt 1 = cô
            if (Objects.equals(relativeDTO.getRelation(), "uncle 1") || Objects.equals(relativeDTO.getRelation(), "uncle 2") || Objects.equals(relativeDTO.getRelation(), "aunt 1")){
                // Set cha, mẹ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "paternal grandfather")){
                        genogramDTO.setF_key(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "paternal grandmother")){
                        genogramDTO.setM_key(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có môí quan hệ là anh, chị em ruột của mẹ
            // uncle 3 = cậu, aunt 2 = dì
            if (Objects.equals(relativeDTO.getRelation(), "uncle 3") || Objects.equals(relativeDTO.getRelation(), "aunt 2")){
                // Set cha, mẹ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "maternal grandfather")){
                        genogramDTO.setF_key(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "maternal grandmother")){
                        genogramDTO.setM_key(r1.getRid());
                    }
                }
            }

            // Nếu người điền form là nam
            if (Objects.equals(genogram.getSex(), "male")){
                // Nếu thân nhân có mối quan hệ con cái
                if (Objects.equals(relativeDTO.getRelation(), "child")) {
                    // Set khóa cha, mẹ
                    genogramDTO.setF_key(genogram.getKey());
                    for (Relative r1 : relativeList){
                        if (Objects.equals(r1.getRelation(), "wife")){
                            genogramDTO.setM_key(r1.getRid());
                        }
                    }
                }

            }

            // Nếu người điền form là nữ
            if (Objects.equals(genogram.getSex(), "female")){
                // Nếu thân nhân có mối quan hệ con cái
                if (Objects.equals(relativeDTO.getRelation(), "child")) {
                    // Set khóa cha, mẹ
                    genogramDTO.setM_key(genogram.getKey());
                    for (Relative r1 : relativeList){
                        if (Objects.equals(r1.getRelation(), "husband")){
                            genogramDTO.setF_key(r1.getRid());
                        }
                    }
                }

                //

            }

            Genogram genogram1 = modelMapper.map(genogramDTO,Genogram.class);
            genogramList.add(genogram1);
        }

        genogramNodeRepository.saveAll(genogramList);
    }
}
