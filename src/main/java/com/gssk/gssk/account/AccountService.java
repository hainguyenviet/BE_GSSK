package com.gssk.gssk.account;

import com.gssk.gssk.account.Account;
import com.gssk.gssk.account.AccountRepository;
import com.gssk.gssk.registration.token.ConfirmationToken;
import com.gssk.gssk.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //return accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG)));
        List<SimpleGrantedAuthority> roles = null;
        Account account = accountRepository.findByEmail(email);
        if (account != null){
            roles = Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString()));
            return new User(account.getUsername(), account.getPassword(), roles);
        }
        else throw new UsernameNotFoundException("User not found");
    }

    public String signUpUser(Account account) {
        Account userExists = accountRepository.findByEmail(account.getEmail());
        if (userExists != null) {
            throw new IllegalStateException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(account.getPassword());

        account.setPassword(encodedPassword);

        accountRepository.save(account);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), account);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableAccount(String email){
        return accountRepository.enableAccount(email);
    }
}
