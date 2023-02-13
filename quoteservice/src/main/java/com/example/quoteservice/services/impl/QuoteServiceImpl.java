package com.example.quoteservice.services.impl;


import com.example.quoteservice.model.NoSuchEntityException;
import com.example.quoteservice.model.Quote;
import com.example.quoteservice.model.UserDataBaseException;
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

    private final String HOST_NAME_USER = "http://localhost:8081/service/";
    private final String HOST_NAME_VOTE = "http://localhost:8083/service/";


    @Autowired
    public QuoteServiceImpl(QuotesRepository quotesRepository, RestTemplateBuilder builder) {
        this.quotesRepository = quotesRepository;
        this.restTemplate = builder.build();
    }

    @Override
    public Quote createQuote(String content, Long userId) {
        Quote quote = new Quote();
        log.info("Try to save quotes");
        quote.setContent(content);
        quote.setUserAccountId(userId);
        quote.setDateOfCreation(Timestamp.valueOf(LocalDateTime.now()));
        quotesRepository.save(quote);
        String url = HOST_NAME_USER + "setQuote?userId=" + userId + "&quoteId=" + quote.getId();
        try {
            restTemplate.put(url, String.class);
        } catch (HttpClientErrorException e) {
            throw new UserDataBaseException("Exception in setting quote to user");
        }
        return quote;

    }

    @Override
    public void deleteQuote(Long quoteId, Long userId) {
        Quote quote = quotesRepository.findById(quoteId).orElseThrow(() -> new NoSuchEntityException(String.format("Quote with id = %d does not exist", quoteId)));
        checkAuthority(userId, quote);
        String url = HOST_NAME_USER + "deleteQuote?userId=" + userId + "&quoteId=" + quoteId;
        try {
            restTemplate.put(url, String.class);
        } catch (HttpClientErrorException e) {
            throw new UserDataBaseException("Exception in deleting quote from user");
        }
        quotesRepository.delete(quote);
    }


    @Override
    public Quote getQuote(Long id) {
        log.info("Finding quote with id = " + id);
        Quote quote = quotesRepository.findById(id).orElseThrow(() -> new NoSuchEntityException(String.format("Quote with id = %d does not exist", id)));
        return quote;
    }

    @Override
    public Quote getRandomQuote() {
        log.info("Getting random quote");
        List<Quote> quoteList = quotesRepository.findAll();
        Quote quote = quoteList.stream().findAny().orElseThrow(() -> new NoSuchEntityException(String.format("Quotes are empty")));
        return quote;
    }

    @Override
    public Quote changeQuote(String content, Long quoteId, Long userId) {
        Quote quote = quotesRepository.findById(quoteId).orElseThrow(() -> new NoSuchEntityException(String.format("Quote with id = %d does not exist", quoteId)));
        checkAuthority(userId, quote);
        quote.setContent(content);
        quote.setDateOfUpdate(Timestamp.valueOf(LocalDateTime.now()));
        log.info("Changing quote with id = {} and author = {}", quoteId, userId);
        return quotesRepository.save(quote);
    }

    @Override
    public String deleteVoteFromQuote(Long quoteId, Long voteId) {
        log.info("delete vote id = {} from quote id = {}", voteId, quoteId);
        Quote quote = getQuote(quoteId);
        Set<Long> newVotes = quote.getVotes();
        newVotes.remove(quoteId);
        quote.setVotes(newVotes);
        quote = quotesRepository.save(quote);
        return quote.toString();

    }

    @Override
    public String setVoteToQuote(Long quoteId, Long voteId) {
        log.info("Set vote id = {} to quote id = {}", voteId, quoteId);
        Quote quote = getQuote(quoteId);
        Set<Long> newVotes = quote.getVotes();
        newVotes.add(quoteId);
        quote.setVotes(newVotes);
        quote = quotesRepository.save(quote);
        return quote.toString();
    }

    @Override
    public String getInformation(Long quoteId) {
        Quote quote = quotesRepository.findById(quoteId).orElseThrow(() -> new NoSuchEntityException(String.format("Quote with id = %d does not exist", quoteId)));
        String url = HOST_NAME_VOTE + "/result?quoteId=" + quoteId;
        int result = restTemplate.getForObject(url, int.class);
        String text = "Quote{" +
                ", content='" + quote.getContent() + '\'' +
                ", dateOfCreation=" + quote.getDateOfCreation() + '\'' +
                ", dateOfUpdate=" + quote.getDateOfUpdate() + '\'' +
                ", result=" + result + '\'' +
                '}';

        return text;
    }



    @Override
    public List<Quote> getWorstQuotes() {
        String url = HOST_NAME_VOTE + "/worst";
        return getQuotesList(url);
    }

    @Override
    public List<Quote> getBestQuotes() {
        String url = HOST_NAME_VOTE + "/best";
        return getQuotesList(url);
    }

    private List<Quote> getQuotesList(String url) {
        try {
            ResponseEntity<Long[]> response =
                    restTemplate.getForEntity(
                            url,
                            Long[].class);
            Long[] list = response.getBody();

            List<Quote> quoteList = new ArrayList<>();
            for (Long id :
                    list) {
                quoteList.add(getQuote(id));
            }
            return quoteList;

        } catch (HttpClientErrorException e) {
            throw new UserDataBaseException("Exception in vote base");
        }
    }


    private void checkAuthority(Long userId, Quote quote) {
        if (!Objects.equals(quote.getUserAccountId(), userId)) {
            throw new IllegalAccessError("This user has not written such quote");
        }
    }


}
