package com.example.quoteservice.services;


import com.example.quoteservice.model.Quote;

import java.util.List;

public interface QuoteService {

    Quote createQuote(String content, Long userId);
    void deleteQuote(Long quoteId, Long userId);
    Quote getQuote(Long id);
    Quote getRandomQuote();

    Quote changeQuote(String content, Long quoteId, Long userId);


    String deleteVoteFromQuote(Long quoteId, Long voteId);

    String setVoteToQuote(Long quoteId, Long voteId);

    String getInformation(Long quoteId);

    List<Quote> getWorstQuotes();

    List<Quote> getBestQuotes();
}
