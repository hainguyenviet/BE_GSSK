package com.gssk.gssk.account;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " + "SET a.enabled = TRUE WHERE a.email=?1" )
    int enableAccount(String email);
}
