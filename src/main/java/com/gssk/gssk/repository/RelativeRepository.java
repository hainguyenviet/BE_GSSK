package com.gssk.gssk.repository;

import com.gssk.gssk.model.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RelativeRepository extends JpaRepository<Relative, String> {
}

