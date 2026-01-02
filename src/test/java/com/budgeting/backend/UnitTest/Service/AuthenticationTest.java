package com.budgeting.backend.UnitTest.Service;

import com.budgeting.backend.dto.in.SignIn;
import com.budgeting.backend.dto.in.SignUp;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.service.AuthenticationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Map;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationTest {

    @Autowired
    AuthenticationService authenticationService;

    User user;

    User primaryUser = null;

    // we will create a user before we ran any test
    @BeforeEach
    public void testSetUp(){
        SignUp sign = new SignUp();
        sign.setName("Nikhil Sharma");
        sign.setEmail("nikhil@example.com");
        sign.setPassword("Password123!");

        Map<String, Object> result = authenticationService.signUp(sign);

        String message = result.get("message").toString();
        Assertions.assertEquals(message, "User created Successfully","We should be getting this message");

        primaryUser = authenticationService.getUserByEmail("nikhil@example.com");
    }

    @AfterEach
    public void testTearDown(){
        if (primaryUser != null) {
            authenticationService.deleteUser(primaryUser.getId());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "nikhil@example.com, Password123!",
            "test@example.com, Test@456",
            "hello@example.com, Hello@789",
            "hello@example.com,Hello@78923232323232323232323222323223232323223232323223",
            "hello@example.com,hello"
    })
    public void TestingDifferentScenarioWhileSignUp(String email, String password){
        try{
            SignUp sign = new SignUp();
            sign.setName("Nikhil Sharma");
            sign.setEmail("nikhil@example.com");
            sign.setPassword("Password123!");

            Map<String, Object> result = authenticationService.signUp(sign);
            Assertions.assertTrue(false,"We should never hit this code");
        }catch (IllegalArgumentException e){
            Assertions.assertTrue(true,"We should hit this area of code");
        }
    }

    @Test
    public void SignIn(){
        SignIn signIn = new SignIn();
        signIn.setEmail(primaryUser.getEmail());
        signIn.setPassword(primaryUser.getPassword());
        MockHttpServletResponse response = new MockHttpServletResponse();

        Map<String,Object> result = authenticationService.signIn(signIn,response);

        Assertions.assertNotNull(response.getCookie("refreshToken"),"Must have a refresh Token");

        Assertions.assertNotNull(result.get("message"),"Must have a some value here");
    }

    @Test
    public void DeleteUser(){
        // first we create the user
        String email ="nikhil@e.com";

        SignUp sign = new SignUp();
        sign.setName("Nikhil Sharma");
        sign.setEmail(email);
        sign.setPassword("Password123!");

        Map<String, Object> result = authenticationService.signUp(sign);

        User temp = authenticationService.getUserByEmail(email);

        // now we delete it
        authenticationService.deleteUser(temp.getId());

        //now we try to check again if user exist or not
        Assertions.assertNull(authenticationService.getUserByEmail(email),"This user must be deleted by now");
    }

    @Test
    public void UpdateUserPassword(){
        // first we create the user
        String email ="nikhil@e.com";

        SignUp sign = new SignUp();
        sign.setName("Nikhil Sharma");
        sign.setEmail(email);
        sign.setPassword("Password123!");

        Map<String, Object> result = authenticationService.signUp(sign);

        User temp = authenticationService.getUserByEmail(email);

        SignIn signIn = new SignIn();
        signIn.setPassword("Test1@1@1");
        signIn.setEmail(email);
        // now we delete it
        authenticationService.resetPasswd(signIn);

        MockHttpServletResponse response = new MockHttpServletResponse();

        result = authenticationService.signIn(signIn,response);

        Assertions.assertNotNull(response.getCookie("refreshToken"),"Must have a refresh Token");

        Assertions.assertNotNull(result.get("message"),"Must have a some value here");
    }
}
