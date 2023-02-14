package com.example.userservice.controller;

import com.example.userservice.model.dto.UserDto;
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
    public UserDto createUser(@RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String password) {
        log.info("Create user with name = {}, email = {}", name, email);
        return userAccountService.createUser(name, email, password);

    }

    @GetMapping("{id}")
    public UserDto getUser(@RequestParam Long id) {
        log.info("Getting user with id = {}", id);
        return userAccountService.getUser(id);
    }


}
