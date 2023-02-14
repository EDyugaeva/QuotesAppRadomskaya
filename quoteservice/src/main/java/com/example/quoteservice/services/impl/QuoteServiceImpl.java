package com.example.quoteservice.services.impl;


import com.example.quoteservice.model.dto.QuoteDto;
import com.example.quoteservice.model.exceptions.NoSuchEntityException;
import com.example.quoteservice.model.entity.Quote;
import com.example.quoteservice.model.exceptions.UserDataBaseException;
import com.example.quoteservice.model.mapper.QuoteMapper;
import com.example.quoteservice.repository.QuotesRepository;
import com.example.quoteservice.services.QuoteService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class QuoteServiceImpl implements QuoteService {
    private RestTemplate restTemplate;
    private final QuotesRepository quotesRepository;

    private final QuoteMapper quoteMapper;
    //TODO change localhost to constant
    private final String HOST_NAME_USER = "http://localhost:8081/service/";
    private final String HOST_NAME_VOTE = "http://localhost:8083/service/";


    @Autowired
    public QuoteServiceImpl(QuotesRepository quotesRepository, RestTemplateBuilder builder, QuoteMapper quoteMapper) {
        this.quotesRepository = quotesRepository;
        this.restTemplate = builder.build();
        this.quoteMapper = quoteMapper;
    }

    /**
     * Creating and saving quotes
     *
     * @param content - text for Quote
     * @param userId  - author
     * @return Quote representation. Could throw an exception if the user does not exist or there are problem with base
     */
    @Override
    public QuoteDto createQuote(String content, Long userId) {
        Quote quote = new Quote();
        log.info("Try to save quotes");
        quote.setContent(content);
        quote.setUserAccountId(userId);
        quote.setDateOfCreation(Timestamp.valueOf(LocalDateTime.now()));
        quotesRepository.save(quote);
        String url = HOST_NAME_USER + "setQuote?userId=" + userId + "&quoteId=" + quote.getId();
        try {
            log.info("Send response to user service");
            restTemplate.put(url, Void.class);
        } catch (HttpClientErrorException e) {
            throw new UserDataBaseException("Exception in setting quote to user");
        }
        return quoteMapper.toQuoteDto(quote);

    }

    /**
     * Deleting quotes. Delete quote from user also.
     * Could throw an exception if the user or quote does not exist or there are problem with base
     *
     * @param quoteId - quote Id
     * @param userId  - author
     */
    @Override
    public void deleteQuote(Long quoteId, Long userId) {
        Quote quote = quotesRepository.findById(quoteId).orElseThrow(() ->
                new NoSuchEntityException(String.format("Quote with id = %d does not exist", quoteId)));
        checkAuthority(userId, quote);
        String url = HOST_NAME_USER + "deleteQuote?userId=" + userId + "&quoteId=" + quoteId;
        try {
            log.info("Send response to user service");
            restTemplate.put(url, Void.class);
        } catch (HttpClientErrorException e) {
            throw new UserDataBaseException("Exception in deleting quote from user");
        }
        quotesRepository.delete(quote);
    }

    /**
     * Finding quote
     *
     * @param id - quote id
     * @return quote dto
     */
    @Override
    public QuoteDto getQuote(Long id) {
        QuoteDto quoteDto = quoteMapper.toQuoteDto(findQuote(id));
        return quoteDto;
    }

    /**
     * Finding quote from base
     *
     * @param id - quote id
     * @return Quote model or exception if this quote does not exist
     */
    public Quote findQuote(Long id) {
        log.info("Finding quote with id = " + id);
        Quote quote = quotesRepository.findById(id).orElseThrow(() ->
                new NoSuchEntityException(String.format("Quote with id = %d does not exist", id)));
        return quote;
    }

    /**
     * Finding random quote
     *
     * @return quote dto or exception if there are no quotes
     */
    @Override
    public QuoteDto getRandomQuote() {
        log.info("Getting random quote");
        List<Quote> quoteList = quotesRepository.findAll();
        if (quoteList.isEmpty()) {
            throw  new NoSuchEntityException(String.format("Quotes are empty"));
        }

        Quote quote = quoteList.get(new Random().nextInt(quoteList.size()-1));
        return quoteMapper.toQuoteDto(quote);
    }

    /**
     * Changing content in quot
     *
     * @param content - new text
     * @param quoteId
     * @param userId
     * @return quote dto or exception if this is not the right author
     */
    @Override
    public QuoteDto changeQuote(String content, Long quoteId, Long userId) {
        Quote quote = findQuote(quoteId);
        checkAuthority(userId, quote);
        quote.setContent(content);
        quote.setDateOfUpdate(Timestamp.valueOf(LocalDateTime.now()));
        log.info("Changing quote with id = {} and author = {}", quoteId, userId);
        quotesRepository.save(quote);
        return quoteMapper.toQuoteDto(quote);
    }

    /**
     * Deleting vote id from quote model(if the vote was deleted).
     * Is needed to make connection between quote and its vote
     * @param quoteId
     * @param voteId
     */
    @Override
    public void deleteVoteFromQuote(Long quoteId, Long voteId) {
        log.info("delete vote id = {} from quote id = {}", voteId, quoteId);
        Quote quote = findQuote(quoteId);
        Set<Long> newVotes = quote.getVotes();
        newVotes.remove(quoteId);
        quote.setVotes(newVotes);
        quote = quotesRepository.save(quote);
        log.info("Quote with id = {} has sunh votes = {}", quoteId, quote.getVotes());
    }

    /**
     * Setting vote id from quote model
     * Is needed to make connection between quote and its vote
     * @param quoteId
     * @param voteId
     */
    @Override
    public void setVoteToQuote(Long quoteId, Long voteId) {
        log.info("Set vote id = {} to quote id = {}", voteId, quoteId);
        Quote quote = findQuote(quoteId);
        Set<Long> newVotes = quote.getVotes();
        newVotes.add(quoteId);
        quote.setVotes(newVotes);
        quote = quotesRepository.save(quote);
        log.info("Quote with id = {} has sunh votes = {}", quoteId, quote.getVotes());
    }

    /**
     * Find the worst quotes
     *
     * @return list of quotes dto
     */
    @Override
    public List<QuoteDto> getWorstQuotes() {
        String url = HOST_NAME_VOTE + "/worst";
        return getQuotesList(url);
    }

    /**
     * Find the best quotes
     *
     * @return list of quotes dto
     */
    @Override
    public List<QuoteDto> getBestQuotes() {
        String url = HOST_NAME_VOTE + "/best";
        return getQuotesList(url);
    }

    /**
     * @param url - link to send response in vote service
     * @return list of quotes dto
     */
    private List<QuoteDto> getQuotesList(String url) {
        try {
            ResponseEntity<Long[]> response =
                    restTemplate.getForEntity(
                            url,
                            Long[].class);
            Long[] list = response.getBody();
            List<QuoteDto> quoteList = new ArrayList<>();
            for (Long id :
                    list) {
                quoteList.add(quoteMapper.toQuoteDto(findQuote(id)));

            }
            return quoteList;

        } catch (HttpClientErrorException e) {
            throw new UserDataBaseException("Exception in vote base");
        }
    }

    /**
     * Checking the correct authority of quotes
     *
     * @param userId
     * @param quote
     */
    private void checkAuthority(Long userId, Quote quote) {
        if (!Objects.equals(quote.getUserAccountId(), userId)) {
            throw new IllegalAccessError("This user has not written such quote");
        }
    }


}
