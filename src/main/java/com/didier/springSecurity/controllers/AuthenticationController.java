package com.didier.springSecurity.controllers;

import com.didier.springSecurity.dtos.LoginUserDto;
import com.didier.springSecurity.dtos.RegisterUserDto;
import com.didier.springSecurity.dtos.RefreshTokenDto;
import com.didier.springSecurity.entities.User;
import com.didier.springSecurity.repositories.UserRepository;
import com.didier.springSecurity.services.AuthenticationService;
import com.didier.springSecurity.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        List<String> role = null;
        if (authenticatedUser != null) {
            role = authenticationService.getUserRole(authenticatedUser.getEmail());
        }

        LoginResponse loginResponse;
        if (role != null) {
            loginResponse = new LoginResponse()
                    .setToken(jwtToken)
                    .setExpiresIn(jwtService.getExpirationTime())
                    .setRole(role);
        } else {
            loginResponse = new LoginResponse()
                    .setToken(jwtToken)
                    .setExpiresIn(jwtService.getExpirationTime());
        }

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User currentUser = userRepository.findByEmail(userEmail).orElse(null);
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        String expiredToken = refreshTokenDto.getExpiredToken();
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(expiredToken);

        return ResponseEntity.ok(refreshTokenResponse);
    }
}
