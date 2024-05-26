package com.didier.springSecurity.services;

import com.didier.springSecurity.controllers.RefreshTokenResponse;
import com.didier.springSecurity.dtos.LoginUserDto;
import com.didier.springSecurity.dtos.RegisterUserDto;
import com.didier.springSecurity.entities.User;
import com.didier.springSecurity.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder,
                                 UserDetailsService userDetailsService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public User signup(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        user.setFullName(registerUserDto.getFullName());
        user.setRole(registerUserDto.getRole());

        // Save user to the database
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public List<String> getUserRole(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
        return Collections.singletonList(user.getRole());
    }

    public RefreshTokenResponse refreshToken(String expiredToken) {
        String userEmail = jwtService.extractUsername(expiredToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (userEmail != null && userDetails != null && jwtService.isTokenValid(expiredToken, userDetails)) {
            String newToken = jwtService.generateToken(userDetails);
            List<String> role = getUserRole(userEmail);
            return new RefreshTokenResponse(newToken, jwtService.getExpirationTime(), role);
        } else {
            throw new IllegalArgumentException("Invalid or expired token for refresh.");
        }
    }
}
