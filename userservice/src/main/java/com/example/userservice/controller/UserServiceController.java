package com.example.userservice.controller;

import com.example.userservice.services.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to send response from quotes and vote services
 */
@RestController
@Slf4j
@RequestMapping(path = "/service")
public class UserServiceController {

    private final UserAccountService userAccountService;

    public UserServiceController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @PutMapping(path = "setQuote")
    public void putQuote(@RequestParam Long userId,
                           @RequestParam Long quoteId) {
        log.info("Try to set quote id {} to user with id {} ", quoteId, userId);
        userAccountService.setQuotes(userId, quoteId);
    }

    @PutMapping(path = "deleteQuote")
    public void deleteQuote(@RequestParam Long userId,
                              @RequestParam Long quoteId) {
        log.info("Try to delete quote id {} from user with id {} ", quoteId, userId);
        userAccountService.deleteQuoteFromUser(userId, quoteId);
    }

    @PutMapping(path = "setVote")
    public void putVote(@RequestParam Long userId,
                           @RequestParam Long voteId) {
        log.info("Try to set vote id {} to user with id {} ", voteId, userId);
       userAccountService.setVoteToUser(userId, voteId);
    }

    @PutMapping(path = "deleteVote")
    public void deleteVote(@RequestParam Long userId,
                              @RequestParam Long voteId) {
        log.info("Try to delete vote id {} from user with id {} ", voteId, userId);
        userAccountService.deleteVoteFromUser(userId, voteId);
    }
}
