package com.energymeter.useridentificationservice.controller;

import com.energymeter.useridentificationservice.dto.UserDto;
import com.energymeter.useridentificationservice.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserCredentialService userCredentialService;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        return userCredentialService.retrieveUser(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto changePassword(@PathVariable long userId, @RequestParam String password) {
        return userCredentialService.changePassword(userId, password);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userCredentialService.deleteUser(userId);
    }


}
