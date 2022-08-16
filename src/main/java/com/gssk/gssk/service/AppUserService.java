package com.gssk.gssk.service;

import com.gssk.gssk.model.*;
import com.gssk.gssk.security.AppUserNotFoundException;
import com.gssk.gssk.security.account.ERole;
import com.gssk.gssk.security.registration.token.ConfirmationToken;
import com.gssk.gssk.security.registration.token.ConfirmationTokenService;
import com.gssk.gssk.repository.AppUserRepository;
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

    private final PersonService personService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email);
        if (user == null && !Objects.equals(email, "admin")){
            throw new UsernameNotFoundException("User not found in DB");
        }
        else {
            List<SimpleGrantedAuthority> authorities = null;
            assert user != null;
            authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
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

        AppUser appUser = appUserRepository.findByEmail(user.getEmail());

        Person person = new Person();
        person.setUsername(appUser.getUsername());
        person.setHealthRecord(new HealthRecord());
        person.setEmail(appUser.getEmail());

        List<Relative> relativeList = new ArrayList<Relative>();
        Relative r = new Relative();
        relativeList.add(r);
        person.setRelativeList(relativeList);

        List<Illness> personIllness = new ArrayList<>();
        personIllness.add(new Illness());
        person.getHealthRecord().setIllnessList(personIllness);

        List<Illness> relativeIllness = new ArrayList<>();
        relativeIllness.add(new Illness());
        r.setIllnessRelative(relativeIllness);

        personService.addNewPerson(person);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public void signUpUserAfterOAuthLoginSuccess(String email, String fullName, String password){
        AppUser appUser = new AppUser();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        appUser.setEmail(email);
        appUser.setFullName(fullName);
        appUser.setPassword(encodedPassword);
        appUser.setEnabled(true);
        appUser.setLocked(false);
        appUser.setRole(ERole.USER);

        appUserRepository.save(appUser);
    }

    public void updateUserAfterOAuthLoginSuccess(AppUser appUser, String name){
        appUser.setFullName(name);
        appUserRepository.save(appUser);
    }
    public AppUser getAccount(String email){ return appUserRepository.findByEmail(email); }

    public Iterable<AppUser> getAllAccounts(){ return  appUserRepository.findAll(); }

    public AppUser getAccountById(Long id) {
        return appUserRepository.findById(id).get();
    }
    public int enableAccount(String email){
        return appUserRepository.enableAccount(email);
    }

    public void deleteAccount(Long id) {
        AppUser appUser = appUserRepository.findById(id).get();
        appUserRepository.delete(appUser);
    }

    public AppUser updateAccount(String username, AppUser appUserRequest){
        AppUser appUser = appUserRepository.findByEmail(username);
        appUser.setFullName(appUserRequest.getFullName());
        appUser.setEmail(appUserRequest.getEmail());
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        appUser.setEnabled(appUserRequest.getEnabled());
        appUser.setLocked(appUserRequest.getLocked());
//         appUser.setUpdateAt(LocalDateTime.now());
        return appUserRepository.save(appUser);
    }


    public void updateResetPasswordToken(String token, String email) throws AppUserNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email);
        if (appUser != null) {
            appUser.setResetPasswordToken(token);
            appUserRepository.save(appUser);
        } else {
            throw new AppUserNotFoundException("User not found in DB");
        }
    }

    public AppUser getByResetPasswordToken(String token) {
        return appUserRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(AppUser appUser, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        appUser.setPassword(encodedPassword);

        appUser.setResetPasswordToken(null);
        appUserRepository.save(appUser);
    }
}