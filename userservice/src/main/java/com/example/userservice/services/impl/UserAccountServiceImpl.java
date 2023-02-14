package com.example.userservice.services.impl;


import com.example.userservice.model.dto.UserDto;
import com.example.userservice.model.exceptions.NoSuchEntityException;
import com.example.userservice.model.entity.UserAccount;
import com.example.userservice.model.mapper.UserMapper;
import com.example.userservice.repository.UserAccountRepository;
import com.example.userservice.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    private final UserMapper userMapper;


    @Override
    public UserDto createUser(String name, String email, String password) {
        if (!userAccountRepository.findUserAccountByEmail(email).isEmpty()) {
            throw new IllegalArgumentException("This email is registered");
        }
        UserAccount savingUser = new UserAccount();
        savingUser.setName(name);
        savingUser.setEmail(email);
        savingUser.setPassword(password);
        savingUser.setDateOfCreation(Timestamp.valueOf(LocalDateTime.now()));
        log.info("Saving user with name = {}", name);
        userAccountRepository.save(savingUser);
        return userMapper.toUserDto(savingUser);
    }

    @Override
    public UserDto getUser(Long id) {
        UserAccount userAccount = findUserAccount(id);
        return userMapper.toUserDto(userAccount);
    }

    /**
     * Is searching for user in repository
     * @param userId
     * @return user model or throws exception (if there is not such user)
     */
    private UserAccount findUserAccount(Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).orElseThrow(() ->
                new NoSuchEntityException(String.format("User with id = %d does not exist", userId)));
        log.info("User with id = {} is found", userAccount);
        return userAccount;
    }

    /**
     * Setting quote id to user model. Is needed to make connection between quote and its author
     * @param userId
     * @param quoteId
     * @return String of user representation. Could throw an exception if the user does not exist
     */
    @Override
    public String setQuotes(Long userId, Long quoteId) {
        log.info("Set quote id = {} to user id = {}", quoteId, userId);
        UserAccount userAccount = findUserAccount(userId);
        Set<Long> newQuotes = userAccount.getQuotes();
        newQuotes.add(quoteId);
        userAccount.setQuotes(newQuotes);
        userAccount = userAccountRepository.save(userAccount);
        return userAccount.toString();
    }
    /**
     * Deleting quote id from user model(if the quote was deleted). Is needed to make connection between quote and its author
     * @param userId
     * @param quoteId
     * @return String of user representation. Could throw an exception if the user does not exist
     */
    @Override
    public String deleteQuoteFromUser(Long userId, Long quoteId) {
        log.info("Delete quote id = {} from user id = {}", quoteId, userId);
        UserAccount userAccount = findUserAccount(userId);
        Set<Long> newQuotes = userAccount.getQuotes();
        newQuotes.remove(quoteId);
        userAccount.setQuotes(newQuotes);
        userAccount = userAccountRepository.save(userAccount);
        return userAccount.toString();
    }
    /**
     * Setting vote id to user model
     * Is needed to make connection between vote and its author
     * @param userId
     * @param voteId
     * @return String of user representation. Could throw an exception if the user does not exist
     */
    @Override
    public String setVoteToUser(Long userId, Long voteId) {
        log.info("Set vote id = {} to user id = {}", voteId, userId);
        UserAccount userAccount = findUserAccount(userId);
        Set<Long> newVotes = userAccount.getVotes();
        newVotes.add(voteId);
        userAccount.setVotes(newVotes);
        userAccount = userAccountRepository.save(userAccount);
        return userAccount.toString();
    }

    /**
     * Deleting vote id from user model(if the vote was deleted - that happens when the grade is changing)
     * Is needed to make connection between vote and its author
     * @param userId
     * @param voteId
     * @return String of user representation. Could throw an exception if the user does not exist
     */
    @Override
    public String deleteVoteFromUser(Long userId, Long voteId) {
        log.info("Delete vote id = {} from user id = {}", voteId, userId);
        UserAccount userAccount = findUserAccount(userId);
        Set<Long> newVotes = userAccount.getVotes();
        newVotes.remove(voteId);
        userAccount.setVotes(newVotes);
        userAccount = userAccountRepository.save(userAccount);
        return userAccount.toString();
    }


}
