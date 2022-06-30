package com.gssk.gssk.service;

import com.gssk.gssk.model.HealthRecord;
import com.gssk.gssk.model.Illness;
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


        // update health record
        HealthRecord newHealthRecord = personRequest.getHealthRecord(), healthRecord = person.getHealthRecord();
        newHealthRecord.setId(healthRecord.getId());
        List<Illness> newPersonIllness = newHealthRecord.getIllnessList(), personIllness = healthRecord.getIllnessList();
        Iterator<Illness> it1 = newPersonIllness.iterator(), it2 = personIllness.iterator();
        while(it1.hasNext() && it2.hasNext()){
            it1.next().setId(it2.next().getId());
        }
        person.setHealthRecord(newHealthRecord);

        // update relative
        List<Relative> newRelativeList = personRequest.getRelativeList();
        List<Relative> relativeList = person.getRelativeList();
        List<Relative> toRemove = new ArrayList<>();
        List<Relative> duplicate = new ArrayList<>();
        if (newRelativeList.isEmpty()){
            relativeList.clear();
        }
        else{
            for (Relative r : relativeList) {
                for (Relative nr : newRelativeList) {
                    if (Objects.equals(r.getRelation(), nr.getRelation())) {
                        r.setName(nr.getName());
                        r.setAge(nr.getAge());
                        r.setGender(nr.getGender());
                        r.setDeath_age(nr.getDeath_age());
                        r.setDeathCause(nr.getDeathCause());
                        r.setFamilyOrder(nr.getFamilyOrder());
                        r.setFamilyOrderOther(nr.getFamilyOrderOther());
                        r.setIsDead(nr.getIsDead());
                        if (nr.getIllnessRelative() != null){
                            if (!r.getIllnessRelative().isEmpty()){
                                List<Illness> oldList = r.getIllnessRelative(), newList = nr.getIllnessRelative();
                                Iterator<Illness> oldIt = oldList.iterator(), newIt = newList.iterator();
                                while(oldIt.hasNext() && newIt.hasNext()){
                                    newIt.next().setId(oldIt.next().getId());
                                }
                                r.setIllnessRelative(newList);
                            }else {
                                r.setIllnessRelative(nr.getIllnessRelative());
                            }
                        }
                        else {
                            r.getIllnessRelative().clear();
                        }
                        duplicate.add(nr);
                        break;
                    } else if (newRelativeList.indexOf(nr) == (newRelativeList.size() - 1) && (!Objects.equals(r.getRelation(), nr.getRelation()))) {
                        toRemove.add(r);
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
