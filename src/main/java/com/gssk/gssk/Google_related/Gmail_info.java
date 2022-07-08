package com.gssk.gssk.Google_related;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.context.annotation.Bean;

public class Gmail_info {



    @Bean
    public GoogleAuthenticator gAuth() {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        // googleAuthenticator.setCredentialRepository();
        return googleAuthenticator;
    }
}
