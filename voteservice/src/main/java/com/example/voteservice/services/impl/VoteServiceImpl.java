package com.example.voteservice.services.impl;


import com.example.voteservice.model.exception.NoSuchEntityException;
import com.example.voteservice.model.exception.DataBaseException;
import com.example.voteservice.model.entity.Vote;
import com.example.voteservice.repository.VoteRepository;
import com.example.voteservice.services.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;


@Service
@Slf4j
public class VoteServiceImpl implements VoteService {

    private RestTemplate restTemplate;

    private final VoteRepository voteRepository;

    private final String HOST_NAME_USER = "http://localhost:8081/service/";
    private final String HOST_NAME_QUOTE = "http://localhost:8082/service/";


    public VoteServiceImpl(VoteRepository voteRepository, RestTemplateBuilder builder) {
        this.voteRepository = voteRepository;
        this.restTemplate = builder.build();
    }

    //TODO quoteService, userService
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Vote upVote(Long quoteId, Long userId) {
        Vote vote = voteRepository.getVoteBy(quoteId, userId).orElse(new Vote());
        if (vote.getQuote() != null) {
            if (vote.getGrade() == 1) {
                log.info("Vote does exist");
                throw new IllegalAccessError("Vote for this quote was made");
            } else {
                log.info("Vote does exist and is different");
                deleteVote(vote);
            }
        }

        vote.setAuthor(userId);
        vote.setQuote(quoteId);
        vote.setGrade(1);
        vote.setCreatedAt(LocalDate.now());
        log.info("Up vote");
        voteRepository.save(vote);
        setVoteToUserAndQuote(userId, quoteId, vote);

        return vote;
    }

    //TODO quoteService, userService
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Vote downVote(Long quoteId, Long userId) {
        Vote vote = voteRepository.getVoteBy(quoteId, userId).orElse(new Vote());
        if (vote.getQuote() != null) {
            if (vote.getGrade() == -1) {
                log.info("Vote does exist");
                throw new IllegalAccessError("Vote for this quote was made");
            } else {
                log.info("Vote does exist and is different");
                deleteVote(vote);
            }
        }
        vote.setAuthor(userId);
        vote.setQuote(quoteId);
        vote.setGrade(-1);
        vote.setCreatedAt(LocalDate.now());
        log.info("Down vote");

        voteRepository.save(vote);
        setVoteToUserAndQuote(userId, quoteId, vote);

        return vote;

    }

    @Override
    public void deleteVote(Vote vote) {
        deleteVoteFromUserAndQuote(vote.getAuthor(), vote.getQuote(), vote);
        voteRepository.delete(vote);
    }

    private void deleteVoteFromUserAndQuote(Long userId, Long quoteId, Vote vote) {
        String urlUser = HOST_NAME_USER + "deleteVote?userId=" + userId + "&voteId=" + vote.getId();
        String urlQuote = HOST_NAME_QUOTE + "deleteVote?quoteId=" + quoteId + "&voteId=" + vote.getId();
//        log.info(urlQuote);
//        log.info(urlUser);
        try {
            restTemplate.put(urlUser, String.class);
            restTemplate.put(urlQuote, String.class);

        } catch (HttpClientErrorException e) {
            throw new DataBaseException("Exception in setting vote to user or quote");
        }
    }

    private void setVoteToUserAndQuote(Long userId, Long quoteId, Vote vote) {
        String urlUser = HOST_NAME_USER + "setVote?userId=" + userId + "&voteId=" + vote.getId();
        String urlQuote = HOST_NAME_QUOTE + "setVote?quoteId=" + quoteId + "&voteId=" + vote.getId();
//        log.info(urlQuote);
//        log.info(urlUser);

        try {
            restTemplate.put(urlUser, String.class);
            restTemplate.put(urlQuote, String.class);
        } catch (HttpClientErrorException e) {
            throw new DataBaseException("Exception in setting vote to user or quote");
        }
    }

    //TODO changing in quoteService to get all information about quotes
    @Override
    public int getResultVoteFromQuoteId(long quoteId) {
//        Vote vote = voteRepository.findFirstByQuoteId((quoteId)).orElseThrow(() -> new NoSuchEntityException(String.format("Vote with quote id = %d does not exist", quoteId)));
        Vote vote = voteRepository.findFirstByQuoteId((quoteId)).orElse(new Vote());
        if (vote.getAuthor() == null) {
            log.info("Quote is not estimated");
            return 0;
        }
        return voteRepository.getGradeByQuote(quoteId);
    }

    //TODO quoteService
    @Override
    public List<Long> getTopTen() {
        List<Long> longList = voteRepository.findTopTenQuery();
        checkingExistingVotes(longList);
        return longList;
    }

    //TODO quoteService
    @Override
    public List<Long> getWorseTen() {
        List<Long> longList = voteRepository.findWorseTenQuery();
        checkingExistingVotes(longList);
        return longList;
    }

    //TODO quoteService
    @Override
    public Map<String, Integer> getGraphForQuote(Long quoteId) {
        Map<String, Integer> map = new LinkedHashMap<>();
        LocalDate startDate = voteRepository.findTheFirstDateOfVotesFromQuoteWithId(quoteId);
        LocalDate actualDate = startDate;
        int sum = 0;
        while (actualDate.isBefore(LocalDate.now().plusDays(1))) {
            Integer temp = voteRepository.getGradeByQuoteAndDate(quoteId, actualDate);
            if (temp == null) {
                temp = 0;
            }
            sum = sum + temp;
            map.put(actualDate.toString(), sum);
            actualDate = actualDate.plusDays(1);
        }
        return map;
    }


    private void checkingExistingVotes(List<Long> longList) {
        if (longList.isEmpty()) {
            throw new NoSuchEntityException("There are not votes");
        }
    }


}
