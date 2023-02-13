package com.example.userservice.controller;

import com.example.userservice.model.UserAccount;
import com.example.userservice.services.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path = "/user")
public class UserController {
    private final UserAccountService userAccountService;

    public UserController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PostMapping
    public UserAccount createUser(@RequestParam String name,
                                  @RequestParam String email,
                                  @RequestParam String password) {
        log.info("Create user");
        return userAccountService.createUser(name, email, password);

    }

    @GetMapping
    public UserAccount getUser(@RequestParam Long id) {
        log.info("Getting user with id = ", id);
        return userAccountService.getUser(id);
    }


}
