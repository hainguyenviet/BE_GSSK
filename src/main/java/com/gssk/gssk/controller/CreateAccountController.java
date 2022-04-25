package com.gssk.gssk.controller;

import com.gssk.gssk.model.Account;
import com.gssk.gssk.service.CreateAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
public class CreateAccountController {
    @Autowired
    CreateAccountService createAccountService;

    @PostMapping(produces = "application/json")
    public Account createAccount(@RequestBody Account account){ return createAccountService.createAccount(account); }
}
