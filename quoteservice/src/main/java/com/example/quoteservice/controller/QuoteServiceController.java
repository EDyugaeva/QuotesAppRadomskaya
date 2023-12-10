package com.example.quoteservice.controller;

import com.example.quoteservice.model.dto.QuoteVoteDto;
import com.example.quoteservice.services.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to send response from vote service
 */
@RestController
@Slf4j
@RequestMapping(path = "/service")
public class QuoteServiceController {

    private final QuoteService quoteService;

    public QuoteServiceController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PatchMapping(path = "setVote")
    public void putVote(@RequestBody QuoteVoteDto quoteVoteDto) {
        log.info("Try to set vote id {} to quote with id {} ", quoteVoteDto.getVoteId(), quoteVoteDto.getQuoteId());
        quoteService.setVoteToQuote(quoteVoteDto);
    }

    @PatchMapping(path = "deleteVote")
    public void deleteVote(@RequestBody QuoteVoteDto quoteVoteDto) {
        log.info("Try to delete vote id {} from user with id {} ", quoteVoteDto.getVoteId(), quoteVoteDto.getQuoteId());
        quoteService.deleteVoteFromQuote(quoteVoteDto);
    }
}
