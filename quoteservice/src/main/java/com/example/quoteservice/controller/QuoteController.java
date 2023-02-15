package com.example.quoteservice.controller;

import com.example.quoteservice.model.dto.QuoteDto;
import com.example.quoteservice.services.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controller to interact with quotes (get, post, put, delete)
 */
@RestController
@RequestMapping(path = "/quote")
@Slf4j
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public QuoteDto createQuote(@RequestParam String content,
                                @RequestParam Long id) {
        log.info("Creating quotes");
        return quoteService.createQuote(content, id);

    }

    @GetMapping("{id}")
    public QuoteDto getQuote(@PathVariable Long id) {
        log.info("Getting  quotes");
        return quoteService.getQuote(id);
    }

    @GetMapping(path = "/random")
    public QuoteDto getRandomQuote() {
        log.info("Getting random quotes");
        return quoteService.getRandomQuote();

    }

    @PutMapping
    public QuoteDto changeQuote(@RequestParam String content,
                             @RequestParam Long quoteId,
                             @RequestParam Long userId) {
        return quoteService.changeQuote(content, quoteId, userId);
    }

    @DeleteMapping
    public void deleteMapping(@RequestParam Long quoteId,
                              @RequestParam Long userId) {
        quoteService.deleteQuote(quoteId, userId);
    }

    @GetMapping(path = "/best")
    public List<QuoteDto> getBestQuotes() {
        return quoteService.getBestQuotes();
    }

    @GetMapping(path = "/worst")
    public List<QuoteDto> getWorstQuotes() {
        return quoteService.getWorstQuotes();
    }



}
