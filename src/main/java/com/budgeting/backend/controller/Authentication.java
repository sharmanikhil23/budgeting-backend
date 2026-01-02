package com.budgeting.backend.controller;

import com.budgeting.backend.dto.in.SignIn;
import com.budgeting.backend.dto.in.SignUp;
import com.budgeting.backend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class Authentication {

    private final AuthenticationService authenticationService;

    @Autowired
    public Authentication(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUp signUp) {

        Map<String, Object> body = authenticationService.signUp(signUp);

        return new ResponseEntity<>(body,HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody SignIn signIn,
            HttpServletResponse response
    ) {

        Map<String, Object> body =
                authenticationService.signIn(signIn, response);

        return new ResponseEntity<>(body,HttpStatus.OK);
    }

    @PostMapping("/reset-passwd")
    public ResponseEntity<?> resetPasswd( @Valid @RequestBody SignIn signIn){
        Map<String, Object> body = authenticationService.resetPasswd(signIn);
        return new ResponseEntity<>(body,HttpStatus.OK);
    }
}
