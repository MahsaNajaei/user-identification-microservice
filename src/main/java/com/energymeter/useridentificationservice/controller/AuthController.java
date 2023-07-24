package com.energymeter.useridentificationservice.controller;

import com.energymeter.useridentificationservice.dto.AuthenticationRequest;
import com.energymeter.useridentificationservice.entity.UserCredential;
import com.energymeter.useridentificationservice.exception.ApplicationSecurityException;
import com.energymeter.useridentificationservice.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthController {

    private final UserCredentialService userCredentialService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserCredential userCredential) {
        userCredentialService.addUser(userCredential);
    }

    @PostMapping("/authenticate")
    public String getToken(@RequestBody AuthenticationRequest authenticationRequest) throws ApplicationSecurityException {
        try {
            var authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            authenticationManager.authenticate(authentication);
            return userCredentialService.getToken(authenticationRequest.getUsername());
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            e.printStackTrace();
            throw new ApplicationSecurityException("Sorry! Username or password is incorrect!");
        }
    }

    @GetMapping("/validate-token")
    public boolean validateToken(@RequestParam String authorization) {
        return userCredentialService.isTokenValid(authorization);
    }

}
