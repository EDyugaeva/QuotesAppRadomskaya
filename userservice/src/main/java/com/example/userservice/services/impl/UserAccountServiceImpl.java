package com.example.userservice.services.impl;


import com.example.userservice.model.dto.QuoteUserDto;
import com.example.userservice.model.dto.UserDto;
import com.example.userservice.model.dto.UserRegistrationDto;
import com.example.userservice.model.dto.VoteUserDto;
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
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {
        String email = userRegistrationDto.getEmail();
        if (userAccountRepository.findUserAccountByEmail(email).isPresent()) {
            throw new IllegalArgumentException("This email is registered");
        }
        String name = userRegistrationDto.getName();
        UserAccount savingUser = new UserAccount();
        savingUser.setName(name);
        savingUser.setEmail(email);
        String password = userRegistrationDto.getPassword();

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
     *
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
     *
     */
    @Override
    public void setQuotes(QuoteUserDto quoteUserDto) {
        log.info("Set quote id = {} to user id = {}", quoteUserDto.getQuoteId(), quoteUserDto.getUserId());
        UserAccount userAccount = findUserAccount(quoteUserDto.getUserId());
        Set<Long> newQuotes = userAccount.getQuotes();
        newQuotes.add(quoteUserDto.getQuoteId());
        userAccount.setQuotes(newQuotes);
        userAccount = userAccountRepository.save(userAccount);
        log.info("User wih id {} has such quotes: {}", quoteUserDto.getUserId(), userAccount.getQuotes());
    }

    /**
     * Deleting quote id from user model(if the quote was deleted). Is needed to make connection between quote and its author
     * Could throw an exception if the user does not exist
     */
    @Override
    public void deleteQuoteFromUser(QuoteUserDto quoteUserDto) {
        log.info("Delete quote id = {} from user id = {}", quoteUserDto.getQuoteId(), quoteUserDto.getUserId());
        UserAccount userAccount = findUserAccount(quoteUserDto.getUserId());
        Set<Long> newQuotes = userAccount.getQuotes();
        newQuotes.remove(quoteUserDto.getQuoteId());
        userAccount.setQuotes(newQuotes);
        userAccount = userAccountRepository.save(userAccount);
        log.info("User wih id {} has such quotes: {}", quoteUserDto.getUserId(), userAccount.getQuotes());
    }

    /**
     * Setting vote id to user model
     * Is needed to make connection between vote and its author
     * Could throw an exception if the user does not exist
     */
    @Override
    public void setVoteToUser(VoteUserDto voteUserDto) {
        log.info("Set vote id = {} to user id = {}", voteUserDto.getVoteId(), voteUserDto.getUserId());
        UserAccount userAccount = findUserAccount(voteUserDto.getUserId());
        Set<Long> newVotes = userAccount.getVotes();
        newVotes.add(voteUserDto.getVoteId());
        userAccount.setVotes(newVotes);
        userAccount = userAccountRepository.save(userAccount);
        log.info("User wih id {} has such votes: {}", voteUserDto.getUserId(), userAccount.getVotes());
    }

    /**
     * Deleting vote id from user model(if the vote was deleted - that happens when the grade is changing)
     * Is needed to make connection between vote and its author
     * Could throw an exception if the user does not exist
     */
    @Override
    public void deleteVoteFromUser(VoteUserDto voteUserDto) {
        log.info("Delete vote id = {} from user id = {}", voteUserDto.getVoteId(), voteUserDto.getUserId());
        UserAccount userAccount = findUserAccount(voteUserDto.getUserId());
        Set<Long> newVotes = userAccount.getVotes();
        newVotes.remove(voteUserDto.getVoteId());
        userAccount.setVotes(newVotes);
        userAccount = userAccountRepository.save(userAccount);
        log.info("User wih id {} has such votes: {}", voteUserDto.getUserId(), userAccount.getVotes());
    }
}
