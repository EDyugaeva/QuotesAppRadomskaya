package com.example.quoteservice.controller;

import com.example.quoteservice.services.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(path = "/service")
public class QuoteServiceController {

    private final QuoteService quoteService;


    public QuoteServiceController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }


    @PutMapping(path = "setVote")
    public String putVote(@RequestParam Long quoteId,
                          @RequestParam Long voteId) {
        log.info("Try to set vote id {} to user with id {} ", voteId, quoteId);
        return quoteService.setVoteToQuote(quoteId, voteId);
    }

    @PutMapping(path = "deleteVote")
    public String deleteVote(@RequestParam Long quoteId,
                             @RequestParam Long voteId) {
        log.info("Try to delete vote id {} from user with id {} ", voteId, quoteId);
        return quoteService.deleteVoteFromQuote(quoteId, voteId);
    }
}
