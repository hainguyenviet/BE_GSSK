package com.gssk.gssk.Google_related;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Types;


@Configuration
@RequiredArgsConstructor
public class Gmail_info {

    private final CredentialRepository credentialRepository;

    @Bean
    public GoogleAuthenticator gAuth() {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        //credentialRepository.setUserInfo(new user_info(gmail,null, Types.INTEGER,null));
         googleAuthenticator.setCredentialRepository(credentialRepository);
        return googleAuthenticator;
    }
}
