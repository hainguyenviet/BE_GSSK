package com.gssk.gssk.repository;

import com.gssk.gssk.model.Relative;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelativeRepository extends CrudRepository<Relative, String> {
    @Query("SELECT e FROM tbl_relative e WHERE e.relation like ?1")
    List<Relative> findByRelationLike(String relation);

}

