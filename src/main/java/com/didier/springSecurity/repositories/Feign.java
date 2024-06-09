package com.didier.springSecurity.repositories;

import com.didier.springSecurity.controllers.LoginResponse;
import com.didier.springSecurity.controllers.RefreshTokenResponse;
import com.didier.springSecurity.dtos.LoginUserDto;
import com.didier.springSecurity.dtos.RefreshTokenDto;
import com.didier.springSecurity.dtos.RegisterUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.didier.springSecurity.entities.User;

@FeignClient(name = "SPRINGSECURITY")
public interface Feign {
    @PostMapping("/auth/signup")
    public User register(@RequestBody RegisterUserDto registerUserDto);

    @PostMapping("/auth/login")
    public LoginResponse authenticate(@RequestBody LoginUserDto loginUserDto);

    @GetMapping("/auth/users")
    public User getCurrentUser();

    @PostMapping("/auth/refresh")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenDto refreshTokenDto);
}
