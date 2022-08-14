package com.gssk.gssk.security.account;

import com.gssk.gssk.model.AppUser;
import com.gssk.gssk.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommandLineAppStartUpRunner implements CommandLineRunner {
    @Autowired
    AppUserRepository appUserRepository;

    @Override
    public void run(String... args) throws Exception {
        String email = "admin";
        if (appUserRepository.findByEmail(email) == null){
           AppUser admin = new AppUser("admin", "$2a$12$0ZbZfxnwHAlWbtXl79k/Dusf4DvUsd2tov6/5LngpYgnYQrkIjg3e", ERole.ADMIN, false, true, LocalDateTime.now());
           appUserRepository.save(admin);
        }
    }


}
