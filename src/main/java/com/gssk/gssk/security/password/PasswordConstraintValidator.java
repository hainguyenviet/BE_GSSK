package com.gssk.gssk.security.password;

import lombok.SneakyThrows;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
//        Properties properties = new Properties();
//        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("passay.properties");
//        try {
//            properties.load(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        MessageResolver resolver = new PropertiesMessageResolver(properties);
        PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 24),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()));

        RuleResult ruleResult = validator.validate(new PasswordData(password));
        if (ruleResult.isValid()){
            System.out.print("true");
            return true;
        }
        List<String>  messages = validator.getMessages(ruleResult);
        String messageTemplate = String.join(",", messages);
        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation().disableDefaultConstraintViolation();

        return false;
    }
}
