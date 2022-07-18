package com.gssk.gssk.controller;

import com.gssk.gssk.model.ResetPasswordRequest;
import com.gssk.gssk.security.AppUserNotFoundException;
import com.gssk.gssk.security.registration.RegistrationRequest;
import com.gssk.gssk.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping(value = "/forgot_password", produces = "application/json")
    public String forgotPassword(@RequestBody RegistrationRequest request) throws AppUserNotFoundException {
        return resetPasswordService.forgotPassword(request);
    }

    @PostMapping(value = "/reset_password", produces = "application/json")
    public String resetPassword(@RequestBody ResetPasswordRequest request){
        return resetPasswordService.resetPassword(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return resetPasswordService.confirmToken(token);
    }
}