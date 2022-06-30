package com.gssk.gssk.service;

import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.PersonRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
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
        List<Relative> newRelativeList = personRequest.getRelativeList();
        List<Relative> relativeList = person.getRelativeList();
        List<Relative> toRemove = new ArrayList<>();
        List<Relative> duplicate = new ArrayList<>();

        if (newRelativeList.isEmpty()){
            relativeList.clear();
        }
        else{
            for (Relative nr : relativeList) {
                for (Relative r : newRelativeList) {
                    if (Objects.equals(nr.getRelation(), r.getRelation())) {
                        nr.setName(r.getName());
                        nr.setAge(r.getAge());
                        nr.setGender(r.getGender());
                        nr.setDeath_age(r.getDeath_age());
                        nr.setDeathCause(r.getDeathCause());
                        nr.setFamilyOrder(r.getFamilyOrder());
                        nr.setFamilyOrderOther(r.getFamilyOrderOther());
                        nr.setIsDead(r.getIsDead());
                        nr.setIllnessRelative(r.getIllnessRelative());
                        duplicate.add(r);
                        break;
                    } else if (newRelativeList.indexOf(r) == (newRelativeList.size() - 1) && (!Objects.equals(nr.getRelation(), r.getRelation()))) {
                        toRemove.add(nr);
                    }
                }
            }
            relativeList.removeAll(toRemove);
            newRelativeList.removeAll(duplicate);
            relativeList.addAll(newRelativeList);
        }
        return personRepository.save(person);
    }


    public void deletePerson(Long id){
        Person person = personRepository.findById(id).get();
        personRepository.delete(person);
    }

}
