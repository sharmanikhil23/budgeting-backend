package com.budgeting.backend.UnitTest.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordHelper {

    @Test
    void samePasswordShouldProduceDifferentHashesIfSalted() {
        String first = com.budgeting.backend.global.PasswordHelper.encode("test1");
        String second = com.budgeting.backend.global.PasswordHelper.encode("test1");

        // Check that both encoded passwords are not equal
        assertNotEquals(first, second, "Password using decrypt should be different");
    }

    @Test
    void samePasswords(){
        String encyptedPassword = com.budgeting.backend.global.PasswordHelper.encode("test1");
        String plainPassword = "test1";

        assertTrue(com.budgeting.backend.global.PasswordHelper.checkPassword(plainPassword,encyptedPassword));
    }

    @Test
    void passwordStrengthChecker(){
        String weakPassword1= "test";

        assertThrows(
                IllegalArgumentException.class,
                () -> com.budgeting.backend.global.PasswordHelper.validatePassword(weakPassword1),
                "Weak password should throw IllegalArgumentException"
        );


        String weakPassword2 = "Test";

        assertThrows(
                IllegalArgumentException.class,
                () -> com.budgeting.backend.global.PasswordHelper.validatePassword(weakPassword2),
                "Weak password should throw IllegalArgumentException"
        );

        String weakPassword3 = "Test1";
        assertThrows(
                IllegalArgumentException.class,
                () -> com.budgeting.backend.global.PasswordHelper.validatePassword(weakPassword3),
                "Weak password should throw IllegalArgumentException"
        );

        String weakPassword4 = "Test1!";
        assertThrows(
                IllegalArgumentException.class,
                () -> com.budgeting.backend.global.PasswordHelper.validatePassword(weakPassword4),
                "Weak password should throw IllegalArgumentException"
        );

        String weakPassword5 = "Test1!23232323232323jkdvkjvbekbvekuhwcv34242jkvuvuvu";
        assertThrows(
                IllegalArgumentException.class,
                () -> com.budgeting.backend.global.PasswordHelper.validatePassword(weakPassword4),
                "Weak password should throw IllegalArgumentException"
        );

        assertTrue(com.budgeting.backend.global.PasswordHelper.validatePassword("Test1@1@1"),"This is the good " +
                "strength password");
    }
}
