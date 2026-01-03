package com.budgeting.backend.global;

import org.passay.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;

import java.util.List;

@Component
public class PasswordHelper{

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private static PasswordValidator passwordValidator = new PasswordValidator(new LengthRule(8, 30),
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            new CharacterRule(EnglishCharacterData.Digit, 1),
            // Corrected: Added SpecialCharacterRule and fixed commas
            new CharacterRule(EnglishCharacterData.Special, 1),
            new WhitespaceRule());

    public static String encode(String password){
        return encoder.encode(password);
    }

    public static Boolean checkPassword(String plain, String hash){
        return encoder.matches(plain,hash);
    }

    public static boolean validatePassword(String password){
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if(result.isValid()){
            return true;
        }else{
            List<String> details = passwordValidator.getMessages(result);
            throw new IllegalArgumentException(details.toString());
        }
    }

    public static String generateInviteCode() {
        return java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
