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




        if (Objects.equals(person.getGender(), "male")){
            genogram.setS("M");
        }
        else if (Objects.equals(person.getGender(), "female")){
            genogram.setS("F");
        }
        if (Objects.equals(genogram.getS(), "male")){
            for (Relative r : relativeList){
                if (Objects.equals(r.getRelation(), "wife")){
                    genogram.setUx(r.getRid());
                }
            }
        }
        for (Relative r : relativeList){
            if (Objects.equals(r.getRelation(), "father")){
                genogram.setF(r.getRid());
            }
        }
        for (Relative r : relativeList){
            if (Objects.equals(r.getRelation(), "mother")){
                genogram.setM(r.getRid());
            }
        }

        genogram.setA(setRelativeAttr(attributes));
        int len = 8;
        StringBuilder sb = new StringBuilder(len);
        for (int i=0; i<len; i++){
            sb.append(Generator.charAt(rnd.nextInt(Generator.length())));
        }
        genogram.setListID(sb.toString());
        genogramList.add(genogram);

        // Truyền thông tin thân nhân sang genogram

        for (Relative r : relativeList){


            RelativeDTO relativeDTO = modelMapper.map(r, RelativeDTO.class);
            GenogramDTO genogramDTO = new GenogramDTO();
            genogramDTO.setKey(relativeDTO.getRid());
            genogramDTO.setN(relativeDTO.getName());
            attributes = relativeDTO.getIllnessName();
            if (Objects.equals(relativeDTO.getGender(), "male")){
                genogramDTO.setS("M");
            }
            else if (Objects.equals(relativeDTO.getGender(), "female")){
                genogramDTO.setS("F");
            }

            if( r.getDeath_age()>-1)
              attributes.add("DEAD");

            genogramDTO.setA(setRelativeAttr(attributes));
            genogramDTO.setListID(genogram.getListID());

            // Nếu thân nhân có mối quan hệ là cha
            if (Objects.equals(genogramDTO.getKey(), genogram.getF())){
                // Set khóa cha, mẹ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "paternal grandfather")){
                        genogramDTO.setF(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "paternal grandmother")){
                        genogramDTO.setM(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "mother")){
                        genogramDTO.setUx(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là mẹ
            if (Objects.equals(genogramDTO.getKey(), genogram.getM())){
                // Set khóa cha, mẹ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "maternal grandfather")){
                        genogramDTO.setF(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "maternal grandmother")){
                        genogramDTO.setM(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "father")){
                        genogramDTO.setVir(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là anh, em trai ruột
            if (Objects.equals(relativeDTO.getRelation(), "brother")){
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "father")){
                        genogramDTO.setF(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "mother")){
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là chị, em gái ruột
            if (Objects.equals(relativeDTO.getRelation(), "sister")){
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "father")){
                        genogramDTO.setF(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "mother")){
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là ông nội
            if (Objects.equals(relativeDTO.getRelation(), "paternal grandfather")){
                // Set vợ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "paternal grandmother")){
                        genogramDTO.setUx(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là bà nội
            if (Objects.equals(relativeDTO.getRelation(), "paternal grandmother")){
                // Set chồng
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "paternal grandfather")){
                        genogramDTO.setVir(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là ông ngoại
            if (Objects.equals(relativeDTO.getRelation(), "maternal grandfather")){
                // Set vợ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "maternal grandmother")){
                        genogramDTO.setUx(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là bà ngoại
            if (Objects.equals(relativeDTO.getRelation(), "maternal grandmother")){
                // Set vợ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "maternal grandfather")){
                        genogramDTO.setVir(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có mối quan hệ là anh, chị em ruột của cha
            // uncle 1 = bác trai, uncle 2 = chú, aunt 1 = cô
            if (Objects.equals(relativeDTO.getRelation(), "uncle 1") || Objects.equals(relativeDTO.getRelation(), "uncle 2") || Objects.equals(relativeDTO.getRelation(), "aunt 1")){
                // Set cha, mẹ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "paternal grandfather")){
                        genogramDTO.setF(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "paternal grandmother")){
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }

            // Nếu thân nhân có môí quan hệ là anh, chị em ruột của mẹ
            // uncle 3 = cậu, aunt 2 = dì
            if (Objects.equals(relativeDTO.getRelation(), "uncle 3") || Objects.equals(relativeDTO.getRelation(), "aunt 2")){
                // Set cha, mẹ
                for (Relative r1 : relativeList){
                    if (Objects.equals(r1.getRelation(), "maternal grandfather")){
                        genogramDTO.setF(r1.getRid());
                    }
                    if (Objects.equals(r1.getRelation(), "maternal grandmother")){
                        genogramDTO.setM(r1.getRid());
                    }
                }
            }

            // Nếu người điền form là nam
            if (Objects.equals(genogram.getS(), "male")){
                // Nếu thân nhân có mối quan hệ con cái
                if (Objects.equals(relativeDTO.getRelation(), "child")) {
                    // Set khóa cha, mẹ
                    genogramDTO.setF(genogram.getKey());
                    for (Relative r1 : relativeList){
                        if (Objects.equals(r1.getRelation(), "wife")){
                            genogramDTO.setM(r1.getRid());
                        }
                    }
                }

            }

            // Nếu người điền form là nữ
            if (Objects.equals(genogram.getS(), "female")){
                // Nếu thân nhân có mối quan hệ con cái
                if (Objects.equals(relativeDTO.getRelation(), "child")) {
                    // Set khóa cha, mẹ
                    genogramDTO.setM(genogram.getKey());
                    for (Relative r1 : relativeList){
                        if (Objects.equals(r1.getRelation(), "husband")){
                            genogramDTO.setF(r1.getRid());
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
                result.add(disease_amount+item);
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

    public List<String> addRelativeAttr(String attr,List<String>attrList_target)
    {
        //Dead sẽ là ưu tiên hàng đầu để xét thêm vào attr
        //Lưu ý chỉ add attr của dead vào hàng cuối nhưng đừng lo, cái này sẽ xếp luôn dead vào hàng cuối
        List<String> result=attrList_target;
        boolean redundant=false;

        for (String item:result
        ) {
            if (item.equals(attr) ) {
                redundant = true;
            }
        }
        if (redundant)
            return result;

        if (attr.equals("DEAD"))
        {
            result.add(attr);
        }
        else
        {

            //Note: Cứ để "ME" vào bất cứ khi nào chả sao, vì sẽ có sắp xếp sau đó ở dưới
            if(attrList_target.size() >1)
                result.add(0,attr);
            else
                result.add(attr);
        }

        boolean flag_me=false;
        //cái này là để trả ME về hàng đầu chứ sắp xếp lại thấy phiền quá
        if(attrList_target.size()>1)
            for (String item:result
                 ) {
                    if (Objects.equals(item, "ME"))
                    {
                        result.remove("ME");
                        flag_me=true;
            }
        }

        if (flag_me)
            result.add(0,"ME");

        return result;
    }



}
