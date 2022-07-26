package com.gssk.gssk.repository;

import com.gssk.gssk.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByUsername(String username);
}
