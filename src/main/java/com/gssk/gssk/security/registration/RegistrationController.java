package com.gssk.gssk.security.registration;

import com.gssk.gssk.model.ResetPasswordRequest;
import com.gssk.gssk.security.AppUserNotFoundException;
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

    @PostMapping(value = "/forgot_password", produces = "application/json")
    public String forgotPassword(@RequestBody RegistrationRequest request) throws AppUserNotFoundException {
        return registrationService.forgotPassword(request);
    }

    @PostMapping(value = "/reset_password", produces = "application/json")
    public String resetPassword(@RequestBody ResetPasswordRequest request){
        return registrationService.resetPassword(request);
    }

    @GetMapping(path = "reset_confirm")
    public String resetconfirm(@RequestParam("resettoken") String resettoken){
        return registrationService.confirmResetToken(resettoken);
    }
}
