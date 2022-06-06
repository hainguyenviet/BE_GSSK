//package com.gssk.gssk.registration;
//
//import org.springframework.stereotype.Service;
//
//import java.util.function.Predicate;
//import java.util.regex.Pattern;
//
//@Service
//public class EmailValidator implements Predicate<String> {
//    @Override
//    public boolean test(String email) {
//        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
//        return Pattern.compile(regexPattern).matcher(email).matches();
//    }
//}
