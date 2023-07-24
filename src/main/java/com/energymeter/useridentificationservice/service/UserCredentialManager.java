package com.energymeter.useridentificationservice.service;

import com.energymeter.useridentificationservice.dto.UserDto;
import com.energymeter.useridentificationservice.entity.UserCredential;
import com.energymeter.useridentificationservice.exception.EntityIdNotFoundException;
import com.energymeter.useridentificationservice.repository.UserCredentialRepository;
import com.energymeter.useridentificationservice.security.JwtUtil;
import com.energymeter.useridentificationservice.security.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCredentialManager implements UserCredentialService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ModelMapper mapper;

    //Todo validate users

    @Override
    public UserCredential addUser(UserCredential userCredential) {
        Role role = userCredential.getUsername().startsWith("_admin") ? Role.ADMIN : Role.USER;
        userCredential.setRole(role);
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        return userCredentialRepository.save(userCredential);
    }

    @Override
    public String getToken(String username) {
        return jwtUtil.generateToken(userDetailsService.loadUserByUsername(username));
    }

    @Override
    public boolean isTokenValid(String token) {
        if (token.startsWith("Bearer "))
            token = token.substring(7);
        return jwtUtil.validateToken(token);
    }

    @Override
    public UserDto retrieveUser(long userId) {
        var userCredential = userCredentialRepository.findById(userId).orElseThrow(() -> new EntityIdNotFoundException("User", userId));
        return mapper.map(userCredential, UserDto.class);
    }

    @Override
    public UserDto changePassword(long userId, String password) {
        var userCredential = userCredentialRepository.findById(userId)
                .orElseThrow(() -> new EntityIdNotFoundException("user", userId));
        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        userCredentialRepository.save(userCredential);
        return mapper.map(userCredential, UserDto.class);
    }

    @Override
    public void deleteUser(long userId) {
        userCredentialRepository.deleteById(userId);
    }

}
