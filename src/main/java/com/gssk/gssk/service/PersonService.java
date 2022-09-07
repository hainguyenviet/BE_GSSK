package com.gssk.gssk.service;

import com.gssk.gssk.model.HealthRecord;
import com.gssk.gssk.model.Illness;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.PersonRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;


    public List<Person> getAllPerson(Integer PageNo, Integer PageSize) {
        org.springframework.data.domain.Pageable paging = (org.springframework.data.domain.Pageable) PageRequest.of(PageNo, PageSize);
        Page<Person> result = personRepository.findAll((org.springframework.data.domain.Pageable) paging);
        return result.getContent();
    }

    public Person getPersonByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    @SneakyThrows
    public Person addNewPerson(Person person) {
        return personRepository.save(person);
    }

    public Boolean containsRelation(final List<Relative> list, final String relation){
        return list.stream().map(Relative::getRelation).anyMatch(relation::equals);
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
        person.setUpdateAt(LocalDateTime.now());

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



            if (newRelativeList.isEmpty() && !relativeList.isEmpty()) {
                relativeList.clear();
            } else if (!relativeList.isEmpty()) {
                Relative r = newRelativeList.get(0);
                if (r.getName() == null){
                    r.setRelation("Cha");
                    r.setName("Không rõ tên");
                    r.setGender("Nam");
                    List<Illness> illness = r.getIllnessRelative();
                    Illness i = illness.get(0);
                    i.setName("");

                    Relative mother = new Relative("Mẹ", "Không rõ tên", "Nữ");
                    List<Illness> illness_m = new ArrayList<>();
                    Illness i_m = new Illness();
                    i_m.setName("");
                    illness_m.add(i_m);
                    mother.setIllnessRelative(illness_m);
                    newRelativeList.add(mother);

                    Relative p_grandfather = new Relative("Ông nội", "Không rõ tên", "Nam");
                    List<Illness> illness_pg = new ArrayList<>();
                    Illness i_pg = new Illness();
                    i_pg.setName("");
                    illness_pg.add(i_pg);
                    p_grandfather.setIllnessRelative(illness_pg);
                    newRelativeList.add(p_grandfather);

                    Relative p_grandmother = new Relative("Bà nội", "Không rõ tên", "Nữ");
                    List<Illness> illness_pgm = new ArrayList<>();
                    Illness i_pgm = new Illness();
                    i_pgm.setName("");
                    illness_pgm.add(i_pgm);
                    p_grandmother.setIllnessRelative(illness_pgm);
                    newRelativeList.add(p_grandmother);

                    Relative m_grandfather = new Relative("Ông ngoại", "Không rõ tên", "Nam");
                    List<Illness> illness_mg = new ArrayList<>();
                    Illness i_mg = new Illness();
                    i_mg.setName("");
                    illness_mg.add(i_mg);
                    m_grandfather.setIllnessRelative(illness_mg);
                    newRelativeList.add(m_grandfather);

                    Relative m_grandmother = new Relative("Bà ngoại", "Không rõ tên", "Nữ");
                    List<Illness> illness_mgm = new ArrayList<>();
                    Illness i_mgm = new Illness();
                    i_mgm.setName("");
                    illness_mgm.add(i_mgm);
                    m_grandmother.setIllnessRelative(illness_mgm);
                    newRelativeList.add(m_grandmother);
                }
                else{
                    if (!containsRelation(newRelativeList, "Cha")){
                        Relative father = new Relative("Cha", "Không rõ tên", "Nam");
                        List<Illness> illness = new ArrayList<>();
                        Illness i = new Illness();
                        i.setName("");
                        illness.add(i);
                        father.setIllnessRelative(illness);
                        newRelativeList.add(father);
                    }
                    if (!containsRelation(newRelativeList, "Mẹ")){
                        Relative mother = new Relative("Mẹ", "Không rõ tên", "Nữ");
                        List<Illness> illness = new ArrayList<>();
                        Illness i = new Illness();
                        i.setName("");
                        illness.add(i);
                        mother.setIllnessRelative(illness);
                        newRelativeList.add(mother);
                    }
                    if (!containsRelation(newRelativeList, "Ông nội")){
                        Relative p_grandfather = new Relative("Ông nội", "Không rõ tên", "Nam");
                        List<Illness> illness = new ArrayList<>();
                        Illness i = new Illness();
                        i.setName("");
                        illness.add(i);
                        p_grandfather.setIllnessRelative(illness);
                        newRelativeList.add(p_grandfather);
                    }
                    if (!containsRelation(newRelativeList, "Bà nội")){
                        Relative p_grandmother = new Relative("Bà nội", "Không rõ tên", "Nữ");
                        List<Illness> illness = new ArrayList<>();
                        Illness i = new Illness();
                        i.setName("");
                        illness.add(i);
                        p_grandmother.setIllnessRelative(illness);
                        newRelativeList.add(p_grandmother);
                    }
                    if (!containsRelation(newRelativeList, "Ông ngoại")){
                        Relative m_grandfather = new Relative("Ông ngoại", "Không rõ tên", "Nam");
                        List<Illness> illness = new ArrayList<>();
                        Illness i = new Illness();
                        i.setName("");
                        illness.add(i);
                        m_grandfather.setIllnessRelative(illness);
                        newRelativeList.add(m_grandfather);
                    }
                    if (!containsRelation(newRelativeList, "Bà ngoại")){
                        Relative m_grandmother = new Relative("Bà ngoại", "Không rõ tên", "Nữ");
                        List<Illness> illness = new ArrayList<>();
                        Illness i = new Illness();
                        i.setName("");
                        illness.add(i);
                        m_grandmother.setIllnessRelative(illness);
                        newRelativeList.add(m_grandmother);
                    }
                }
                

                person.setRelativeList(newRelativeList);
                }
            }

        return personRepository.save(person);
    }


    public void deletePerson(Long id){
        Person person = personRepository.findById(id).get();
        personRepository.delete(person);
    }
    
    public Long countAll(){
        Long count = personRepository.count();
        return count;
    }

}
