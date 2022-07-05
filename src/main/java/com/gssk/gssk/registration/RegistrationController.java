package com.gssk.gssk.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
@CrossOrigin
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping(value = "/register", produces = "application/json")
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }
}
