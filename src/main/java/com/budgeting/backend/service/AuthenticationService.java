package com.budgeting.backend.service;

import com.budgeting.backend.dto.in.SignIn;
import com.budgeting.backend.dto.in.SignUp;
import com.budgeting.backend.entity.RefreshTokenEntity;
import com.budgeting.backend.entity.User;
import com.budgeting.backend.global.JWT;
import com.budgeting.backend.global.PasswordHelper;
import com.budgeting.backend.repository.TokenRepository;
import com.budgeting.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordHelper passwordHelper;
    private final JWT jwt;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            TokenRepository tokenRepository,
            PasswordHelper passwordHelper,
            JWT jwt
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordHelper = passwordHelper;
        this.jwt = jwt;
    }

    public Map<String, Object> signUp(SignUp signUp) {

        // Business validation
        if (userRepository.findByEmail(signUp.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Password validation (throws IllegalArgumentException internally)
        passwordHelper.validatePassword(signUp.getPassword());

        signUp.setPassword(passwordHelper.encode(signUp.getPassword()));

        userRepository.save(new User(signUp));

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User created Successfully");
        response.put("error", null);

        return response;
    }

    public Map<String, Object> signIn(SignIn signIn, HttpServletResponse response) {

        User user = userRepository.findByEmail(signIn.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password")
                );

        // Throws IllegalArgumentException if password is wrong
       PasswordHelper.checkPassword(signIn.getPassword(), user.getPassword());

        Map<String, String> tokens = jwt.generateTokens(user.getEmail());

        tokenRepository.save(
                new RefreshTokenEntity(user.getId(), tokens.get("refreshToken"))
        );

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                .httpOnly(true)
                .secure(true) // true in prod
                .path("/auth/refresh")
                .maxAge(jwt.getRefreshExpiration() / 1000)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        Map<String, Object> body = new HashMap<>();
        body.put("status", "success");

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", tokens.get("accessToken"));

        body.put("message", tokenMap);
        body.put("error", null);

        return body;
    }

    public User getUserByEmail(String userEmail) { return userRepository.findByEmail(userEmail).orElse(null); }

    public Map<String, Object> resetPasswd(SignIn signIn) {
        User user = userRepository.findByEmail(signIn.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password")
                );

        // Password validation (throws IllegalArgumentException internally)
        passwordHelper.validatePassword(signIn.getPassword());

        user.setPassword(passwordHelper.encode(signIn.getPassword()));

        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User password update Successfully");
        response.put("error", null);

        return response;
    }

    public Map<String, Object> deleteUser(ObjectId userId){
        User user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User Not Found Try again later")
        );

        userRepository.deleteById(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User deleted Successfully");
        response.put("error", null);

        return response;
    }
}
