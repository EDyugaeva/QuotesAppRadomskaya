package com.example.userservice.services;


import com.example.userservice.model.dto.UserDto;
import com.example.userservice.model.dto.UserRegistrationDto;

public interface UserAccountService {
    UserDto createUser(UserRegistrationDto userRegistrationDto);

    UserDto getUser(Long id);

    void setQuotes(Long userId, Long quoteId);
    void deleteQuoteFromUser(Long userId, Long quoteId);

    void deleteVoteFromUser(Long userId, Long voteId);

    void setVoteToUser(Long userId, Long voteId);


}
