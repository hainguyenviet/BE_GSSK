package com.gssk.gssk.service;

import com.gssk.gssk.model.Account;
import com.gssk.gssk.repository.CreateAccountRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CreateAccountService {
    @Autowired
    CreateAccountRepository createAccountRepository;

    @SneakyThrows
    public Account createAccount(Account account){ return createAccountRepository.save(account); }

    public Account changePassword(String id, Account accountRequest){
        Account account = createAccountRepository.findById(id).get();
        if (!Objects.equals(account.getPassword(), accountRequest.getPassword())){
            account.setPassword(accountRequest.getPassword());
        }
        return createAccountRepository.save(account);
    }

    //public Account getAccountById(String id){ return createAccountRepository.findById(id).get(); }
}
