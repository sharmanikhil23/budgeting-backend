package com.budgeting.backend.UnitTest;

import com.budgeting.backend.dto.in.SignUp;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.repository.UserRepository;
import com.budgeting.backend.service.AuthenticationService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Base {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserRepository userRepository;

    protected  List<User> users;

    public Base(){
        users = new ArrayList<>();
    }

    @BeforeAll
    public void setUpUser() {
        SignUp sign = new SignUp();
        sign.setName("Nikhil Sharma");
        sign.setEmail("nikhil@example.com");
        sign.setPassword("Password123!");

        Map<String, Object> result = authenticationService.signUp(sign);
        Assertions.assertEquals("User created Successfully", result.get("message"));

        users.add(authenticationService.getUserByEmail("nikhil@example.com"));
    }

    @AfterAll
    public void tearDownUser() {
        if (!users.isEmpty()) {
            List<ObjectId> userId = new ArrayList<>();
            for(User u: users){
                userId.add(u.getId());
            }
            userRepository.deleteAllById(userId);
        }
    }

    public User getPrimaryUser(){
        return users.get(0);
    }

    public User getUser(int i){
        return i < users.size() ? users.get(i):null;
    }

    public User createNewUser(){
        String unique = UUID.randomUUID().toString();
        SignUp sign = new SignUp();
        sign.setName("test" + unique);
        sign.setEmail("test" + unique + "@example.com");
        sign.setPassword("Password123!");

        Map<String, Object> result = authenticationService.signUp(sign);
        Assertions.assertEquals("User created Successfully", result.get("message"));

        User newUser = authenticationService.getUserByEmail(sign.getEmail());
        users.add(newUser);
        return newUser;
    }




}
