package com.example.userservice.services;


import com.example.userservice.model.dto.QuoteUserDto;
import com.example.userservice.model.dto.UserDto;
import com.example.userservice.model.dto.UserRegistrationDto;
import com.example.userservice.model.dto.VoteUserDto;

public interface UserAccountService {
    UserDto createUser(UserRegistrationDto userRegistrationDto);
    UserDto getUser(Long id);
    void setQuotes(QuoteUserDto quoteUserDto);
    void deleteQuoteFromUser(QuoteUserDto quoteUserDto);
    void deleteVoteFromUser(VoteUserDto voteToUser);
    void setVoteToUser(VoteUserDto voteToUser);
}
