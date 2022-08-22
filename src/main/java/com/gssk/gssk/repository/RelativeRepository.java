package com.gssk.gssk.repository;

import com.gssk.gssk.model.Relative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RelativeRepository extends JpaRepository<Relative, String> {
    Relative findByRid(long rid);

}

