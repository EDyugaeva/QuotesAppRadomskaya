package com.example.userservice.controller;

import com.example.userservice.model.dto.UserDto;
import com.example.userservice.model.dto.UserRegistrationDto;
import com.example.userservice.services.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to post user and get information about user by id
 */
@RestController
@Slf4j
@RequestMapping(path = "/user")
public class UserController {
    private final UserAccountService userAccountService;

    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        if (userRegistrationDto.getPassword() == null || userRegistrationDto.getEmail() == null || userRegistrationDto.getName() == null) {
            throw new IllegalArgumentException("Exception in creating user");
        }
        log.info("Create user with dto", userRegistrationDto);

        return userAccountService.createUser(userRegistrationDto);

    }

    @GetMapping({"{id}"})
    public UserDto getUser(@PathVariable Long id) {
        log.info("Getting user with id = {}", id);
        return userAccountService.getUser(id);
    }


}
