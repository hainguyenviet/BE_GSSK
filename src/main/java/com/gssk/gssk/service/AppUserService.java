package com.gssk.gssk.service;

import com.gssk.gssk.model.AppUser;
import com.gssk.gssk.model.HealthRecord;
import com.gssk.gssk.model.Person;
import com.gssk.gssk.model.Relative;
import com.gssk.gssk.repository.PersonRepository;
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
private final PersonRepository personRepository;
    private final PersonService personService;




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

        AppUser appUser = appUserRepository.findByEmail(user.getEmail());

        Person person = new Person();
        person.setUsername(appUser.getUsername());
        person.setHealthRecord(new HealthRecord());
        List<Relative> relativeList = new ArrayList<Relative>();
        relativeList.add(new Relative());
        person.setRelativeList(relativeList);

        personService.addNewPerson(person);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
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

    public AppUser updateAccount(Long id, AppUser appUserRequest){
        AppUser appUser = appUserRepository.findById(id).get();
        appUser.setFullName(appUserRequest.getFullName());
        appUser.setEmail(appUserRequest.getEmail());
        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
//        appUser.setPassword(appUserRequest.getPassword());
        appUser.setEnabled(appUserRequest.getEnabled());
        appUser.setLocked(appUserRequest.getLocked());
        return appUserRepository.save(appUser);
    }


    public void OAuthLogin(String email) {

        AppUser existed= appUserRepository.findByEmail(email);
        if (existed==null)
        {
            AppUser newUser=new AppUser(email,"", ERole.USER,false,true);
            Person newPerson= new Person();
            newPerson.setEmail(email);
            personRepository.save(newPerson);
            appUserRepository.save(newUser);
        }
        else
        {
            AppUser newUser=new AppUser(email,"", ERole.USER,false,true);
            Person newPerson= new Person();
            newPerson.setEmail(email);
            personRepository.save(newPerson);
            appUserRepository.save(newUser);
        }
    }
}
