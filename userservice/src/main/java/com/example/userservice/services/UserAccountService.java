package com.example.userservice.services;


import com.example.userservice.model.dto.UserDto;

public interface UserAccountService {
    UserDto createUser(String name, String email, String password);

    UserDto getUser(Long id);

    String setQuotes(Long userId, Long quoteId);
    String deleteQuoteFromUser(Long userId, Long quoteId);

    String deleteVoteFromUser(Long userId, Long voteId);

    String setVoteToUser(Long userId, Long voteId);


}
