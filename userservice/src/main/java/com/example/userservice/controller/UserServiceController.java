package com.example.userservice.controller;

import com.example.userservice.model.dto.QuoteUserDto;
import com.example.userservice.model.dto.VoteUserDto;
import com.example.userservice.services.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping(path = "setQuote")
    public void putQuote(@RequestBody QuoteUserDto quoteUserDto) {
        log.info("Try to set quote id {} to user with id {} ", quoteUserDto.getQuoteId(), quoteUserDto.getUserId());
        userAccountService.setQuotes(quoteUserDto);
    }

    @PatchMapping(path = "deleteQuote")
    public void deleteQuote(@RequestBody QuoteUserDto quoteUserDto) {
        log.info("Try to delete quote id {} from user with id {} ", quoteUserDto.getQuoteId(), quoteUserDto.getUserId());
        userAccountService.deleteQuoteFromUser(quoteUserDto);
    }

    @PatchMapping(path = "setVote")
    public void putVote(@RequestBody VoteUserDto voteUserDto) {
        log.info("Try to set vote id {} to user with id {} ", voteUserDto.getVoteId(), voteUserDto.getUserId());
       userAccountService.setVoteToUser(voteUserDto);
    }

    @PatchMapping(path = "deleteVote")
    public void deleteVote(@RequestBody VoteUserDto voteUserDto) {
        log.info("Try to delete vote id {} from user with id {} ",  voteUserDto.getVoteId(), voteUserDto.getUserId());
        userAccountService.deleteVoteFromUser(voteUserDto);
    }
}
