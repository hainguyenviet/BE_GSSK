package com.gssk.gssk.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.gssk.gssk.model.*;
import com.gssk.gssk.security.account.ERole;
import com.gssk.gssk.service.AppUserService;
import com.gssk.gssk.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    AppUserService appUserService;

    @Autowired
    PersonService personService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getName();

        AppUser appUser = appUserService.getAccount(email);
        if (appUser == null){
            appUserService.signUpUserAfterOAuthLoginSuccess(email, name, "");

            Person person = new Person();
            person.setUsername(email);
            person.setHealthRecord(new HealthRecord());
            List<Relative> relativeList = new ArrayList<Relative>();
            relativeList.add(new Relative());
            person.setRelativeList(relativeList);

            personService.addNewPerson(person);
        }else{
            appUserService.updateUserAfterOAuthLoginSuccess(appUser, name);
        }

        List<SimpleGrantedAuthority> authorities = null;
        authorities = Arrays.asList(new SimpleGrantedAuthority(ERole.USER.toString()));
        User user = new User(oAuth2User.getEmail(), "", authorities);

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(oAuth2User.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

//        String refresh_token = JWT.create()
//                .withSubject(user.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
//                .withIssuer(request.getRequestURL().toString())
//                .sign(algorithm);
        response.setHeader("access_token", access_token);
//        response.setHeader("refresh_token", refresh_token);
        response.setHeader("username", oAuth2User.getEmail());



    }
}
