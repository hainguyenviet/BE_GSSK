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

import java.util.*;


@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    public Iterable<Person> getAllPerson() {
        return personRepository.findAll();
    }

    public Person getPersonByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    @SneakyThrows
    public Person addNewPerson(Person person) {
        return personRepository.save(person);
    }

    public Person updatePerson(String username, Person personRequest) {
        Person person = personRepository.findByUsername(username);
        person.setFirstName(personRequest.getFirstName());
        person.setLastName(personRequest.getLastName());
        person.setDateOfBirth(personRequest.getDateOfBirth());
        person.setIdCard(personRequest.getIdCard());
        person.setEmail(personRequest.getEmail());
        person.setPhoneNumber(personRequest.getPhoneNumber());
        person.setGender(personRequest.getGender());
        person.setUsername(username);

        // update health record
        HealthRecord newHealthRecord = personRequest.getHealthRecord(), healthRecord = person.getHealthRecord();
        if (healthRecord == null) {
            person.setHealthRecord(newHealthRecord);
        } else {
            if (newHealthRecord != null){
                newHealthRecord.setId(healthRecord.getId());
                if (newHealthRecord.getIllnessList() != null){
                    List<Illness> newPersonIllness = newHealthRecord.getIllnessList(), personIllness = healthRecord.getIllnessList();
                    Iterator<Illness> it1 = newPersonIllness.iterator(), it2 = personIllness.iterator();
                    while (it1.hasNext() && it2.hasNext()) {
                        it1.next().setId(it2.next().getId());
                    }
                }
                person.setHealthRecord(newHealthRecord);
            }
            else {
                person.setHealthRecord(null);
            }

        }


        // update relative
        if (personRequest.getRelativeList() != null){
            List<Relative> newRelativeList = personRequest.getRelativeList();
            List<Relative> relativeList = person.getRelativeList();
            List<Relative> toRemove = new ArrayList<>();
            List<Relative> duplicate = new ArrayList<>();
            List<Relative> temp = new ArrayList<>();



            if (newRelativeList.isEmpty() && !relativeList.isEmpty()) {
                relativeList.clear();
            } else if (!relativeList.isEmpty()) {
                if (relativeList.size() == 1){
                    Relative r = relativeList.get(0);
                    if (r.getRelation() == null){
                        person.setRelativeList(newRelativeList);
                    }
                    else {
                        if (newRelativeList.size() == 1){
                            Relative nr = newRelativeList.get(0);
                            if (!Objects.equals(r.getRelation(), nr.getRelation())){
                                relativeList.remove(r);
                                relativeList.add(nr);
                            }
                            else {
                                r.setName(nr.getName());
                                r.setAge(nr.getAge());
                                r.setGender(nr.getGender());
                                r.setDeath_age(nr.getDeath_age());
                                r.setDeathCause(nr.getDeathCause());
                                r.setFamilyOrder(nr.getFamilyOrder());
                                r.setFamilyOrderOther(nr.getFamilyOrderOther());
                                r.setIsDead(nr.getIsDead());
                                if (nr.getIllnessRelative() != null) {
                                    if (!r.getIllnessRelative().isEmpty()) {
                                        List<Illness> oldList = r.getIllnessRelative(), newList = nr.getIllnessRelative();
                                        Iterator<Illness> oldIt = oldList.iterator(), newIt = newList.iterator();
                                        while (oldIt.hasNext() && newIt.hasNext()) {
                                            newIt.next().setId(oldIt.next().getId());
                                        }
                                        r.setIllnessRelative(newList);
                                    } else {
                                        r.setIllnessRelative(nr.getIllnessRelative());
                                    }
                                } else {
                                    r.getIllnessRelative().clear();
                                }
                            }
                        }
                        else {
                            relativeList.remove(r);
                            person.setRelativeList(newRelativeList);
                        }
                    }
                }
                else{
                    if (newRelativeList.size() == 1){
                        relativeList.clear();
                        person.setRelativeList(newRelativeList);
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
                                    if (nr.getIllnessRelative() != null) {
                                        if (!r.getIllnessRelative().isEmpty()) {
                                            List<Illness> oldList = r.getIllnessRelative(), newList = nr.getIllnessRelative();
                                            Iterator<Illness> oldIt = oldList.iterator(), newIt = newList.iterator();
                                            while (oldIt.hasNext() && newIt.hasNext()) {
                                                newIt.next().setId(oldIt.next().getId());
                                            }
                                            r.setIllnessRelative(newList);
                                        } else {
                                            r.setIllnessRelative(nr.getIllnessRelative());
                                        }
                                    } else {
                                        r.getIllnessRelative().clear();
                                    }
                                    newRelativeList.remove(nr);
                                    duplicate.add(r);
                                    break;
                                } else if (newRelativeList.indexOf(nr) == (newRelativeList.size() - 1) && (!Objects.equals(r.getRelation(), nr.getRelation()))) {
                                    toRemove.add(r);
                                }
                            }
                        }
                        relativeList.removeAll(toRemove);;

                        for (Relative r : duplicate){
                            int check = 0;
                            for (Relative cr : relativeList){
                                if (Objects.equals(r.getRelation(), cr.getRelation()) && Objects.equals(r.getName(), cr.getName())
                                        && Objects.equals(r.getGender(), cr.getGender()) && Objects.equals(r.getAge(), cr.getAge())
                                        && Objects.equals(r.getFamilyOrder(), cr.getFamilyOrder()) && Objects.equals(r.getFamilyOrderOther(), cr.getFamilyOrderOther())){
                                    if (check == 0){
                                        check ++;
                                    }
                                    else if (check > 0){
                                        temp.add(cr);
                                    }
                                }
                            }
                            relativeList.removeAll(temp);
                        }
                    }
                 }
                }
            else {
                person.setRelativeList(newRelativeList);
            }
        }
        else {
            List<Relative> relativeList = person.getRelativeList();
            relativeList.clear();
        }

        return personRepository.save(person);
    }


    public void deletePerson(Long id){
        Person person = personRepository.findById(id).get();
        personRepository.delete(person);
    }

}
