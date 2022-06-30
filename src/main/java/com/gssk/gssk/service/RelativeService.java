package com.gssk.gssk.service;

import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.RelativeRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelativeService
{
    @Autowired
    RelativeRepository relativeRepository;
    public Relative UpdateRelative(Long id, Relative relativeRequest)
    {
        Relative relative=relativeRepository.findById(id).get();
        relative.setRelation(relativeRequest.getRelation());
        relative.setName(relativeRequest.getName());
        relative.setGender(relativeRequest.getGender());
        relative.setAge(relativeRequest.getAge());
        relative.setFamilyOrder(relativeRequest.getFamilyOrder());
        relative.setFamilyOrderOther(relativeRequest.getFamilyOrderOther());
        relative.setIsDead(relativeRequest.getIsDead());
        relative.setDeath_age(relativeRequest.getDeath_age());
        relative.setDeathCause(relativeRequest.getDeathCause());
        relative.setIllnessRelative(relativeRequest.getIllnessRelative());


        return relativeRepository.save(relative);
    }

}
