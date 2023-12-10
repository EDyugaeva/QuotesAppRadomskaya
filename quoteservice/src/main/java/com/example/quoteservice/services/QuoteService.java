package com.example.quoteservice.services;


import com.example.quoteservice.model.dto.QuoteCreatingDto;
import com.example.quoteservice.model.dto.QuoteDto;
import com.example.quoteservice.model.dto.QuoteUserDto;
import com.example.quoteservice.model.dto.QuoteVoteDto;

import java.util.List;

public interface QuoteService {
    QuoteDto createQuote(QuoteCreatingDto quoteCreatingDto);
    void deleteQuote(QuoteUserDto quoteUserDto);
    QuoteDto getQuote(Long id);
    QuoteDto getRandomQuote();
    QuoteDto changeQuote(Long quoteId, QuoteCreatingDto quoteCreatingDto);
    void deleteVoteFromQuote(QuoteVoteDto quoteVoteDto);
    void setVoteToQuote(QuoteVoteDto quoteVoteDto);
    List<QuoteDto> getWorstQuotes();
    List<QuoteDto> getBestQuotes();
}
