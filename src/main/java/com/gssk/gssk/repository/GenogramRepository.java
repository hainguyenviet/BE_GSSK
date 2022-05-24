package com.gssk.gssk.repository;

import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.HealthRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface GenogramRepository extends CrudRepository<Genogram,String> {

    @Transactional
    @Modifying
    @Query("SELECT DISTINCT e FROM Genogram g, Genogram e WHERE g.key = ?1 AND g.listID = e.listID ")
    List<Genogram> findByKey (String key);
}
