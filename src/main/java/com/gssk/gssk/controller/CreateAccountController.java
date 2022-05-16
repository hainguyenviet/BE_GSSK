package com.gssk.gssk.controller;

import com.gssk.gssk.model.Account;
import com.gssk.gssk.service.CreateAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class CreateAccountController {
    @Autowired
    CreateAccountService createAccountService;

    @PostMapping(produces = "application/json")
    public Account createAccount(@RequestBody Account account){ return createAccountService.createAccount(account); }

    @PutMapping(value = "/changepw/{id}", produces = "application/json")
    public Account changePassword(@PathVariable("id") String id, Account accountRequest){ return createAccountService.changePassword(id, accountRequest); }
}
