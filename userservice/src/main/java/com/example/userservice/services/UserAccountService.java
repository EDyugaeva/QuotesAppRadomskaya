package com.example.userservice.services;


import com.example.userservice.model.UserAccount;

public interface UserAccountService {
    UserAccount createUser(String name, String email, String password);

    UserAccount getUser(Long id);

    String setQuotes(Long userId, Long quoteId);
    String deleteQuoteFromUser(Long userId, Long quoteId);

    String deleteVoteFromUser(Long userId, Long voteId);


    String setVoteToUser(Long userId, Long voteId);


}
