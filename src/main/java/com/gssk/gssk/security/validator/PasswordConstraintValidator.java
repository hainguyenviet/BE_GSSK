package com.gssk.gssk.security.validator;
import org.passay.*;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
public class PasswordConstraintValidator{
    public boolean checkPassword(String password){
        PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 24), new WhitespaceRule()));

        RuleResult ruleResult = validator.validate(new PasswordData(password));
        if (ruleResult.isValid()){
            return true;
        }

        return false;
    }
}
