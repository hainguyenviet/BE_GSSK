package com.gssk.gssk.security.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
@CrossOrigin
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping(value = "/register", produces = "application/json")
    public void register(@RequestBody RegistrationRequest request){
        registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }
}
