package com.gssk.gssk.Google_related;

import com.warrenstrange.googleauth.GoogleAuthenticator;

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.sql.Types;


@Configuration
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/gmail")


public class Login {
  private final GoogleAuthenticator gAuth;
  private final CredentialRepository credentialRepository;
//   @SneakyThrows
//   @GetMapping("/generate/{username}")
//   public void generate(@PathVariable String username, HttpServletResponse response) {
//       credentialRepository.setUserInfo(new user_info(username,null, Types.INTEGER,null));
//        final GoogleAuthenticatorKey key = gAuth.createCredentials(username);
//        ServletOutputStream outputStream = response.getOutputStream();
//        outputStream.print(key.getKey());
//       outputStream.close();
//        }


    }



