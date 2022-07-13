package com.gssk.gssk.repository;

import com.gssk.gssk.model.AppUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

//    @Query("SELECT a FROM AppUser a WHERE a.email = :email")
//    AppUser findByEmail(@Param("email") String email);
    AppUser findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " + "SET a.enabled = TRUE WHERE a.email=?1" )
    int enableAccount(String email);
}
