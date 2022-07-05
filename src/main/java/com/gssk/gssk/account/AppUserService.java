package com.gssk.gssk.account;

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
import java.util.*;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email);
        if (user == null && !Objects.equals(email, "admin")){
            throw new UsernameNotFoundException("User not found in DB");
        }
        else {
            List<SimpleGrantedAuthority> authorities = null;
            authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString()));
            return new User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    public String signUpUser(AppUser user) {
        AppUser userExists = appUserRepository.findByEmail(user.getEmail());
        if (userExists != null) {
            throw new IllegalStateException("email already taken");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        appUserRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public AppUser getAccount(String email){
        return appUserRepository.findByEmail(email);
    }

    public Iterable<AppUser> getAllAccounts(){
        return  appUserRepository.findAll();
    }

    public int enableAccount(String email){
        return appUserRepository.enableAccount(email);
    }

}
