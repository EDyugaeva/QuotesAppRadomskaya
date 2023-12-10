package com.example.quoteservice.services.impl;


import com.example.quoteservice.model.dto.QuoteCreatingDto;
import com.example.quoteservice.model.dto.QuoteDto;
import com.example.quoteservice.model.dto.QuoteUserDto;
import com.example.quoteservice.model.dto.QuoteVoteDto;
import com.example.quoteservice.model.entity.Quote;
import com.example.quoteservice.model.exceptions.NoSuchEntityException;
import com.example.quoteservice.model.exceptions.UserDataBaseException;
import com.example.quoteservice.model.mapper.QuoteMapper;
import com.example.quoteservice.repository.QuotesRepository;
import com.example.quoteservice.services.QuoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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
    private final RestTemplate restTemplate;
    private final QuotesRepository quotesRepository;
    private final QuoteMapper quoteMapper;

    @Value("${host.userservice}")
    private String USER_HOST;
    @Value("${host.voteservice}")
    private String VOTE_HOST;

    public QuoteServiceImpl(QuotesRepository quotesRepository, RestTemplate restTemplate,
                            QuoteMapper quoteMapper) {
        this.quotesRepository = quotesRepository;
        this.restTemplate = restTemplate;
        this.quoteMapper = quoteMapper;
    }

    /**
     * Creating and saving quotes
     *
     * @return Quote representation. Could throw an exception if the user does not exist or there are problem with base
     */
    @Override
    public QuoteDto createQuote(QuoteCreatingDto quoteCreatingDto) {
        Quote quote = new Quote();
        log.info("Try to save quotes");
        quote.setContent(quoteCreatingDto.getContent());
        quote.setUserAccountId(quoteCreatingDto.getUserId());
        quote.setDateOfCreation(Timestamp.valueOf(LocalDateTime.now()));
        quotesRepository.save(quote);
        QuoteUserDto savingDto = new QuoteUserDto(quote.getId(), quoteCreatingDto.getUserId());
        String url = getUserHost() + "setQuote";
        final HttpEntity<QuoteUserDto> requestEntity = new HttpEntity<>(savingDto);

        try {
            log.info("Send response to user service");
            log.info(url + " with object {}", savingDto);
            restTemplate.patchForObject(url, requestEntity, Void.class);
        } catch (HttpClientErrorException e) {
            log.warn("error while saving new quote", e);
            throw new UserDataBaseException("Exception in setting quote to user");
        }
        return quoteMapper.toQuoteDto(quote);
    }

    /**
     * Deleting quotes. Delete quote from user also.
     * Could throw an exception if the user or quote does not exist or there are problem with base
     */
    @Override
    public void deleteQuote(QuoteUserDto quoteUserDto) {
        Long quoteId = quoteUserDto.getQuoteId();
        Long userId = quoteUserDto.getUserId();
        Quote quote = quotesRepository.findById(quoteId).orElseThrow(() ->
                new NoSuchEntityException(String.format("Quote with id = %d does not exist", quoteId)));
        checkAuthority(userId, quote);
        String url = getUserHost() + "deleteQuote";
        try {
            log.info("Send response to user service with object {}", quoteUserDto);
            log.info(url);
            restTemplate.patchForObject(url, quoteUserDto, Void.class);
        } catch (HttpClientErrorException e) {
            log.warn("error while deleting quote with id = {}", quoteId, e);
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
        return quoteMapper.toQuoteDto(findQuote(id));
    }

    /**
     * Finding quote from base
     *
     * @param id - quote id
     * @return Quote model or exception if this quote does not exist
     */
    public Quote findQuote(Long id) {
        log.info("Finding quote with id = " + id);
        return quotesRepository.findById(id).orElseThrow(() ->
                new NoSuchEntityException(String.format("Quote with id = %d does not exist", id)));
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
            throw new NoSuchEntityException("Quotes are empty");
        }
        Quote quote = quoteList.get(new Random().nextInt(quoteList.size()));
        return quoteMapper.toQuoteDto(quote);
    }

    /**
     * Changing content in quote
     *
     * @return quote dto or exception if this is not the right author
     */
    @Override
    public QuoteDto changeQuote(Long quoteId, QuoteCreatingDto quoteCreatingDto) {
        Quote quote = findQuote(quoteId);
        checkAuthority(quoteCreatingDto.getUserId(), quote);
        quote.setContent(quoteCreatingDto.getContent());
        quote.setDateOfUpdate(Timestamp.valueOf(LocalDateTime.now()));
        log.info("Changing quote with id = {} and author = {}", quoteId, quoteCreatingDto.getUserId());
        quotesRepository.save(quote);
        return quoteMapper.toQuoteDto(quote);
    }

    /**
     * Deleting vote id from quote model(if the vote was deleted).
     * Is needed to make connection between quote and its vote
     */
    @Override
    public void deleteVoteFromQuote(QuoteVoteDto quoteVoteDto) {
        Long voteId = quoteVoteDto.getVoteId();
        Long quoteId = quoteVoteDto.getQuoteId();
        log.info("delete vote id = {} from quote id = {}", voteId, quoteId);
        Quote quote = findQuote(quoteId);
        Set<Long> newVotes = quote.getVotes();
        newVotes.remove(quoteId);
        quote.setVotes(newVotes);
        quote = quotesRepository.save(quote);
        log.info("Quote with id = {} has votes = {}", quoteId, quote.getVotes());
    }

    /**
     * Setting vote id from quote model
     * Is needed to make connection between quote and its vote
     */
    @Override
    public void setVoteToQuote(QuoteVoteDto quoteVoteDto) {
        Long voteId = quoteVoteDto.getVoteId();
        Long quoteId = quoteVoteDto.getQuoteId();
        log.info("Set vote id = {} to quote id = {}", voteId, quoteId);
        Quote quote = findQuote(quoteId);
        Set<Long> newVotes = quote.getVotes();
        newVotes.add(quoteId);
        quote.setVotes(newVotes);
        quote = quotesRepository.save(quote);
        log.info("Quote with id = {} has such votes = {}", quoteId, quote.getVotes());
    }

    /**
     * Find the worst quotes
     *
     * @return list of quotes dto
     */
    @Override
    public List<QuoteDto> getWorstQuotes() {
        String url = getVoteHost() + "/worst";
        return getQuotesList(url);
    }

    /**
     * Find the best quotes
     *
     * @return list of quotes dto
     */
    @Override
    public List<QuoteDto> getBestQuotes() {
        String url = getVoteHost() + "/best";
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

            if (list != null) {
                for (Long id : list) {
                    quoteList.add(quoteMapper.toQuoteDto(findQuote(id)));
                }
                return quoteList;
            }
            throw new NoSuchEntityException("There is no quotes");
        } catch (HttpClientErrorException e) {
            throw new UserDataBaseException("Exception in vote base");
        }
    }

    /**
     * Checking the correct authority of quotes
     */
    private void checkAuthority(Long userId, Quote quote) {
        if (!Objects.equals(quote.getUserAccountId(), userId)) {
            throw new IllegalAccessError("This user has not written such quote");
        }
    }

    /**
     * @return URL to user service
     */
    private String getUserHost() {
        return "http://" + USER_HOST + ":8081/service/";
    }

    /**
     * @return URL to vote service
     */
    private String getVoteHost() {
        return "http://" + VOTE_HOST + ":8083/service/";
    }
}
