package com.example.voteservice.services.impl;


import com.example.voteservice.model.dto.*;
import com.example.voteservice.model.entity.Vote;
import com.example.voteservice.model.exception.DataBaseException;
import com.example.voteservice.model.exception.NoSuchEntityException;
import com.example.voteservice.model.mappers.VoteMapper;
import com.example.voteservice.model.mappers.VoteResultMapper;
import com.example.voteservice.repository.VoteRepository;
import com.example.voteservice.services.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class VoteServiceImpl implements VoteService {
    private final RestTemplate restTemplate;
    private final VoteRepository voteRepository;
    private final VoteResultMapper voteResultMapper;
    private final VoteMapper voteMapper;
    private static final String DELETE_VOTE = "deleteVote";
    private static final String SET_VOTE = "setVote";

    @Value("${host.userservice}")
    private String USER_HOST;

    @Value("${host.quoteservice}")
    private String QUOTE_HOST;

    public VoteServiceImpl(VoteRepository voteRepository, RestTemplate restTemplate,
                           VoteResultMapper voteResultMapper, VoteMapper voteMapper) {
        this.voteRepository = voteRepository;
        this.restTemplate = restTemplate;
        this.voteResultMapper = voteResultMapper;
        this.voteMapper = voteMapper;
    }

    /**
     * Make a vote with grade = 1 (up grade)
     *
     * @return vote dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public VoteResultDto upVote(UserQuoteDto userQuoteDto) {
        Long userId = userQuoteDto.getUserId();
        Long quoteId = userQuoteDto.getQuoteId();

        int grade = 1;
        Vote vote = checkIfVoteIsMade(quoteId, userId, grade);

        vote.setAuthor(userId);
        vote.setQuote(quoteId);
        vote.setGrade(grade);
        vote.setCreatedAt(LocalDate.now());
        log.info("Up vote");
        voteRepository.save(vote);
        setVoteToUserAndQuote(userId, quoteId, vote);
        int result = getResultVoteFromQuoteId(quoteId);

        return voteResultMapper.voteDto(vote, result);
    }

    /**
     * Make a vote with grade = -1 (down grade)
     *
     * @return vote dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VoteResultDto downVote(UserQuoteDto userQuoteDto) {
        Long userId = userQuoteDto.getUserId();
        Long quoteId = userQuoteDto.getQuoteId();

        int grade = -1;

        Vote vote = checkIfVoteIsMade(quoteId, userId, grade);

        vote.setAuthor(userId);
        vote.setQuote(quoteId);
        vote.setGrade(grade);
        vote.setCreatedAt(LocalDate.now());
        voteRepository.save(vote);

        setVoteToUserAndQuote(userId, quoteId, vote);
        int result = getResultVoteFromQuoteId(quoteId);

        return voteResultMapper.voteDto(vote, result);
    }

    /**
     * get result from quote voting
     *
     * @return vote dto (result frade)
     */
    @Override
    public VoteResultDto getVoteFromQuote(Long quoteId) {
        Vote vote = new Vote();
        vote.setQuote(quoteId);
        int result = getResultVoteFromQuoteId(quoteId);
        return voteResultMapper.voteDto(vote, result);
    }

    /**
     * get all quotes to quote
     *
     * @return vote dto
     */
    @Override
    public List<VoteDto> getVotesFromQuote(Long quoteId) {
        List<Vote> listVotes = voteRepository.findVoteByQuote(quoteId);
        List<VoteDto> listDto = new ArrayList<>();
        if (listVotes.isEmpty()) {
            throw new NoSuchEntityException("There are not votes for quote with id = " + quoteId);
        }
        for (Vote v :
                listVotes) {
            listDto.add(voteMapper.toVoteDto(v));
        }

        return listDto;
    }

    /**
     * Check not to give second vote to one user
     *
     * @param grade   +1 if up vote/ -1 if down vote
     */
    private Vote checkIfVoteIsMade(Long quoteId, Long userId, int grade) {
        Vote vote = voteRepository.getVoteBy(quoteId, userId).orElse(new Vote());
        if (vote.getQuote() != null) {
            if (vote.getGrade() == grade) {
                log.info("Vote does exist");
                throw new IllegalAccessError("Vote for this quote was made");
            } else {
                log.info("Vote does exist and is different");
                deleteVote(vote);
            }
        }
        return new Vote();
    }

    @Override
    public void deleteVote(Vote vote) {
        deleteVoteFromUserAndQuote(vote.getAuthor(), vote.getQuote(), vote);
        voteRepository.delete(vote);
    }

    /**
     * delete connection in quotes and users
     *
     */
    private void deleteVoteFromUserAndQuote(Long userId, Long quoteId, Vote vote) {
        String urlUser = getUserHost() + DELETE_VOTE;
        String urlQuote = getQuoteHost() + DELETE_VOTE;
        try {
            restTemplate.patchForObject(urlUser, new VoteUserDto(userId, vote.getId()), Void.class);
            restTemplate.patchForObject(urlQuote, new QuoteVoteDto(quoteId, vote.getId()), Void.class);
        } catch (HttpClientErrorException e) {
            throw new DataBaseException("Exception in setting vote to user or quote");
        }
    }

    /**
     * set vote to quote and user
     *
     */
    private void setVoteToUserAndQuote(Long userId, Long quoteId, Vote vote) {
        String urlUser = getUserHost() + SET_VOTE;
        String urlQuote = getQuoteHost() + SET_VOTE;

        try {
            restTemplate.patchForObject(urlUser, new VoteUserDto(userId, vote.getId()), String.class);
            restTemplate.patchForObject(urlQuote, new QuoteVoteDto(quoteId, vote.getId()),String.class);
        } catch (HttpClientErrorException e) {
            throw new DataBaseException("Exception in setting vote to user or quote");
        }
    }

    /**
     * get result grase to quote
     *
     * @return int result
     */
    public int getResultVoteFromQuoteId(long quoteId) {
        int result = voteRepository.getGradeByQuote(quoteId).orElse(0);
        log.info("Sum grade = {}", result);
        return result;
    }

    /**
     * Find top quotes
     *
     * @return List of top 10 quotes ids
     */
    @Override
    public List<Long> getTopTen() {
        List<Long> longList = voteRepository.findTopTenQuery();
        checkingExistingVotes(longList);
        return longList;
    }

    /**
     * Find top worst quotes
     *
     * @return List of top worst 10 quotes ids
     */
    @Override
    public List<Long> getWorseTen() {
        List<Long> longList = voteRepository.findWorseTenQuery();
        checkingExistingVotes(longList);
        return longList;
    }

    /**
     * Make a map for presenting a graph.
     * X -> date (since creating a quote)
     * Y - sum amount of grades
     *
     * @return Map<String, Integer>
     */
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

    /**
     * check if list of quotes is not empty
     */
    private void checkingExistingVotes(List<Long> longList) {
        if (longList.isEmpty()) {
            throw new NoSuchEntityException("There are not votes");
        }
    }

    /**
     * @return URL to user service
     */
    private String getUserHost() {
        return "http://" + USER_HOST + ":8081/service/";
    }

    /**
     * @return URL to quote service
     */
    private String getQuoteHost() {
        return "http://" + QUOTE_HOST + ":8082/service/";
    }
}
