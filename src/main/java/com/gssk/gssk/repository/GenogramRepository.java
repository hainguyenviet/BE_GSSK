package com.gssk.gssk.repository;

import com.gssk.gssk.model.Genogram;
import com.gssk.gssk.model.HealthRecord;
import org.springframework.data.repository.CrudRepository;

public interface GenogramRepository extends CrudRepository<Genogram,String> {
}
