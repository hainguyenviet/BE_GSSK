package com.gssk.gssk.security.password;
import org.passay.*;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class PasswordConstraintValidator{
    public boolean checkPassword(String password){
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

        return false;
    }
}
