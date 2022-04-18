package com.gssk.gssk.service;

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

    public Relative getRelativeByRelation(String relation) {return relativeRepository.findById(relation).get();}
    
}
