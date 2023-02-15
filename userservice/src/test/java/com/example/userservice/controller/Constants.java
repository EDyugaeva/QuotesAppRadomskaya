package com.example.userservice.controller;

import com.example.userservice.model.entity.UserAccount;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

public class Constants {

    public final static String URL = "http://localhost:";
    public final static String PORT = "8081";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
     public static final String PASSWORD = "password";
    public static final long ID = 1;
    public static final Timestamp TIMESTAMP = Timestamp.valueOf(LocalDateTime.of(2022, 02, 14, 1, 10));

    public static final Set<Long> QUOTES = Set.of(1L, 5L, 15L);

    public static final Set<Long> VOTES = Set.of(2L, 25L, 94L);


    public static final UserAccount USER_ACCOUNT = new UserAccount(ID, NAME, EMAIL, PASSWORD, TIMESTAMP, QUOTES, VOTES);
}
