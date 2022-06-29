package com.gssk.gssk.service;

import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.PersonRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    public Iterable<Person> getAllPerson() {
        return personRepository.findAll();
    }

    public Person getPersonById(Long id) {
        return personRepository.findById(id).get();
    }

    @SneakyThrows
    public Person addNewPerson(Person person) {
        boolean noMother = true, noFather = true, noHusband = true, noWife =true, hasChild = false,
                noPGF = true, noPGM = true, noMGF = true, noMGM = true;
        List<Relative> relativeList = person.getRelativeList();
        for (Relative r : relativeList){
            if (Objects.equals(r.getRelation(), "Cha")){
                noFather = false;
            }
            if (Objects.equals(r.getRelation(), "Mẹ")){
                noMother = false;
            }
            if (Objects.equals(r.getRelation(), "Con ruột")){
                hasChild = true;
            }
            if (Objects.equals(r.getRelation(), "Chồng")){
                noHusband = false;
            }
            if (Objects.equals(r.getRelation(), "Vợ")){
                noWife = false;
            }
            if (Objects.equals(r.getRelation(), "Ông nội")){
                noPGF = false;
            }
            if (Objects.equals(r.getRelation(), "Bà nội")){
                noPGM = false;
            }
            if (Objects.equals(r.getRelation(), "Ông ngoại")){
                noMGF = false;
            }
            if (Objects.equals(r.getRelation(), "Bà ngoại")){
                noMGM = false;
            }
        }
        if (noMother && !noFather){
            Relative setDefaultRelative = new Relative("Mẹ", "Mẹ", "Nữ");
            relativeList.add(setDefaultRelative);
        }
        else if (!noMother && noFather){
            Relative setDefaultRelative = new Relative("Cha", "Cha", "Nam");
            relativeList.add(setDefaultRelative);
        }

        if (hasChild && Objects.equals(person.getGender(), "Nam") && noWife){
            Relative setDefaultRelative = new Relative("Vợ", "Vợ", "Nữ");
            relativeList.add(setDefaultRelative);
        }
        else if (hasChild && Objects.equals(person.getGender(), "Nữ") && noHusband){
            Relative setDefaultRelative = new Relative("Chồng", "Chồng", "Nam");
            relativeList.add(setDefaultRelative);
        }

        if (noPGM && !noPGF){
            Relative setDefaultRelative = new Relative("Bà nội", "Bà nội", "Nữ");
            relativeList.add(setDefaultRelative);
        }
        else if (noPGF && !noPGM){
            Relative setDefaultRelative = new Relative("Ông nội", "Ông nội", "Nam");
            relativeList.add(setDefaultRelative);
        }

        if (noMGM && !noMGF){
            Relative setDefaultRelative = new Relative("Bà ngoại", "Bà ngoại", "Nữ");
            relativeList.add(setDefaultRelative);
        }
        else if (noMGF && !noMGM){
            Relative setDefaultRelative = new Relative("Ông ngoại", "Ông ngoại", "Nam");
            relativeList.add(setDefaultRelative);
        }
        return personRepository.save(person);
    }

    public Person updatePerson(Long id, Person personRequest){
        Person person = personRepository.findById(id).get();
        person.setFirstName(personRequest.getFirstName());
        person.setLastName(personRequest.getLastName());
        person.setDateOfBirth(personRequest.getDateOfBirth());
        person.setIdCard(personRequest.getIdCard());
        person.setEmail(personRequest.getEmail());
        person.setPhoneNumber(personRequest.getPhoneNumber());
        person.setGender(personRequest.getGender());
        person.setHealthRecord(personRequest.getHealthRecord());
        person.setRelativeList(personRequest.getRelativeList());
        return personRepository.save(person);
    }


    public void deletePerson(Long id){
        Person person = personRepository.findById(id).get();
        personRepository.delete(person);
    }

}
