package com.gssk.gssk.controller;
import com.gssk.gssk.security.OAuth2LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/oauth")
public class OAuthController {


    @GetMapping(value = "/success", produces = "application/json")
    public ResponseEntity<String> loginSuccess(@RequestHeader("Cookie") String token) {
        return new ResponseEntity<String>(token, HttpStatus.OK);
    }

}
