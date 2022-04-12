package com.gssk.gssk.service;

import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.RelativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class RelativeService
{
    @Autowired
    RelativeRepository relativeRepository;
    private List<Relative> relatives;

    public Relative getRelativeByRelation(String relation) {return relativeRepository.findById(relation).get();}

    public RelativeService(List<Relative> relativeList) {
        this.relatives = relativeList;
    }
}
