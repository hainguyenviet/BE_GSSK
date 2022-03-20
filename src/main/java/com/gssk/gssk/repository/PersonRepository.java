package com.gssk.gssk.repository;

import com.gssk.gssk.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,String> {


}
