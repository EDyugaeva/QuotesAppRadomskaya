package com.example.userservice.services.impl;


import com.example.userservice.model.NoSuchEntityException;
import com.example.userservice.model.UserAccount;
import com.example.userservice.repository.UserAccountRepository;
import com.example.userservice.services.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }


    @Override
    public UserAccount createUser(String name, String email, String password) {
        if (!userAccountRepository.findUserAccountByEmail(email).isEmpty()) {
            throw new IllegalArgumentException("This email is registered");
        }
        UserAccount savingUser = new UserAccount();
        savingUser.setName(name);
        savingUser.setEmail(email);
        savingUser.setPassword(password);
        savingUser.setDateOfCreation(Timestamp.valueOf(LocalDateTime.now()));
        log.info("Saving user with name = {}", name);
        return userAccountRepository.save(savingUser);
    }

    @Override
    public UserAccount getUser(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(() -> new NoSuchEntityException(String.format("User with id = %d does not exist", id)));
        log.info("User with id = {} is found", id);
        return userAccount;
    }

    @Override
    public String setQuotes(Long userId, Long quoteId) {
        log.info("Set quote id = {} to user id = {}", quoteId, userId);
        UserAccount userAccount = getUser(userId);
        Set<Long> newQuotes = userAccount.getQuotes();
        newQuotes.add(quoteId);
        userAccount.setQuotes(newQuotes);
        userAccount = userAccountRepository.save(userAccount);
        return userAccount.toString();
    }

    @Override
    public String deleteQuoteFromUser(Long userId, Long quoteId) {
        log.info("Delete quote id = {} from user id = {}", quoteId, userId);
        UserAccount userAccount = getUser(userId);
        Set<Long> newQuotes = userAccount.getQuotes();
        newQuotes.remove(quoteId);
        userAccount.setQuotes(newQuotes);
        userAccount = userAccountRepository.save(userAccount);
        return userAccount.toString();
    }

    @Override
    public String deleteVoteFromUser(Long userId, Long voteId) {
        log.info("Delete vote id = {} from user id = {}", voteId, userId);
        UserAccount userAccount = getUser(userId);
        Set<Long> newVotes = userAccount.getVotes();
        newVotes.remove(voteId);
        userAccount.setVotes(newVotes);
        userAccount = userAccountRepository.save(userAccount);
        return userAccount.toString();    }

    @Override
    public String setVoteToUser(Long userId, Long voteId) {
        log.info("Set vote id = {} to user id = {}", voteId, userId);
        UserAccount userAccount = getUser(userId);
        Set<Long> newVotes = userAccount.getVotes();
        newVotes.add(voteId);
        userAccount.setVotes(newVotes);
        userAccount = userAccountRepository.save(userAccount);
        return userAccount.toString();
    }


}
