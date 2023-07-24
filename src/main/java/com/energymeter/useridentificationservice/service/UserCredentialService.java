package com.energymeter.useridentificationservice.service;

import com.energymeter.useridentificationservice.dto.UserDto;
import com.energymeter.useridentificationservice.entity.UserCredential;

public interface UserCredentialService {

    UserCredential addUser(UserCredential userCredential);

    String getToken(String username);

    boolean isTokenValid(String token);

    UserDto retrieveUser(long userId);

    UserDto changePassword(long userId, String password);

    void deleteUser(long userId);
}
