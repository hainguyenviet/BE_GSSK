package com.gssk.gssk.repository;

import com.gssk.gssk.model.Relative;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RelativeRepository extends CrudRepository<Relative, String> {

    @Query(value = "SELECT e FROM Relative e WHERE e.relation like ?1")
    Relative findByRelationIs(String relation);

}

