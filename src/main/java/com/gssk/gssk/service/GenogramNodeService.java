package com.gssk.gssk.service;

import com.gssk.gssk.model.GenogramNode;
import com.gssk.gssk.repository.GenogramNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenogramNodeService {
    @Autowired
    GenogramNodeRepository genogramNodeRepository;
    public Iterable<GenogramNode> getAllNodes(){return genogramNodeRepository.findAll();}
}
