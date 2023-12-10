package com.example.quoteservice.controller;

import com.example.quoteservice.model.dto.QuoteCreatingDto;
import com.example.quoteservice.model.dto.QuoteDto;
import com.example.quoteservice.model.dto.QuoteUserDto;
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
    public QuoteDto createQuote(@RequestBody QuoteCreatingDto quoteCreatingDto) {
        log.info("Creating quote {}", quoteCreatingDto);
        return quoteService.createQuote(quoteCreatingDto);
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

    @PatchMapping
    public QuoteDto changeQuote(@RequestBody QuoteCreatingDto quoteCreatingDto,
                             @RequestParam Long quoteId) {
        return quoteService.changeQuote(quoteId, quoteCreatingDto);
    }

    @DeleteMapping
    public void deleteMapping(@RequestBody QuoteUserDto quoteUserDto) {
        quoteService.deleteQuote(quoteUserDto);
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
