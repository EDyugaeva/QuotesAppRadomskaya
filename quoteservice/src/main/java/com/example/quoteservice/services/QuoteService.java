package com.example.quoteservice.services;


import com.example.quoteservice.model.dto.QuoteDto;

import java.util.List;

public interface QuoteService {

    QuoteDto createQuote(String content, Long userId);
    void deleteQuote(Long quoteId, Long userId);
    QuoteDto getQuote(Long id);
    QuoteDto getRandomQuote();

    QuoteDto changeQuote(String content, Long quoteId, Long userId);


    void deleteVoteFromQuote(Long quoteId, Long voteId);

    void setVoteToQuote(Long quoteId, Long voteId);


    List<QuoteDto> getWorstQuotes();

    List<QuoteDto> getBestQuotes();
}
