package com.energymeter.useridentificationservice.security;

import com.energymeter.useridentificationservice.exception.ApplicationSecurityException;
import com.energymeter.useridentificationservice.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Setter
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserCredentialRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userCredential = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user Not Found With username : " + username));
        return new CustomUserDetails(userCredential);
    }

}
