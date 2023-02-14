package com.example.voteservice.services.impl;


import com.example.voteservice.model.dto.VoteDto;
import com.example.voteservice.model.dto.VoteResultDto;
import com.example.voteservice.model.exception.NoSuchEntityException;
import com.example.voteservice.model.exception.DataBaseException;
import com.example.voteservice.model.entity.Vote;
import com.example.voteservice.model.mappers.VoteMapper;
import com.example.voteservice.model.mappers.VoteResultMapper;
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

    private final VoteResultMapper voteResultMapper;

    private final VoteMapper voteMapper;
//TODO localhost вынести

    private final String HOST_NAME_USER = "http://localhost:8081/service/";
    private final String HOST_NAME_QUOTE = "http://localhost:8082/service/";


    public VoteServiceImpl(VoteRepository voteRepository, RestTemplateBuilder builder, VoteResultMapper voteResultMapper, VoteMapper voteMapper) {
        this.voteRepository = voteRepository;
        this.restTemplate = builder.build();
        this.voteResultMapper = voteResultMapper;
        this.voteMapper = voteMapper;
    }

    /**
     * Make a vote with grade = 1 (up grade)
     *
     * @param quoteId
     * @param userId  - author of the vot (NOT quote)
     * @return vote dto
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public VoteResultDto upVote(Long quoteId, Long userId) {
        int grade = 1;
        Vote vote = checkIfVoteIsMade(quoteId, userId, grade);
        ;

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
     * @param quoteId
     * @param userId  - author of the vot (NOT quote)
     * @return vote dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public VoteResultDto downVote(Long quoteId, Long userId) {
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
     * @param quoteId
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
     * @param quoteId
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
     * @param quoteId
     * @param userId
     * @param grade   +1 if up vote/ -1 if down vote
     * @return
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

    /**
     * delete vote
     *
     * @param vote
     */
    @Override
    public void deleteVote(Vote vote) {
        deleteVoteFromUserAndQuote(vote.getAuthor(), vote.getQuote(), vote);
        voteRepository.delete(vote);
    }

    /**
     * delete connection in quotes and users
     *
     * @param userId
     * @param quoteId
     * @param vote
     */
    private void deleteVoteFromUserAndQuote(Long userId, Long quoteId, Vote vote) {
        String urlUser = HOST_NAME_USER + "deleteVote?userId=" + userId + "&voteId=" + vote.getId();
        String urlQuote = HOST_NAME_QUOTE + "deleteVote?quoteId=" + quoteId + "&voteId=" + vote.getId();

        try {
            restTemplate.put(urlUser, Void.class);
            restTemplate.put(urlQuote, Void.class);

        } catch (HttpClientErrorException e) {
            throw new DataBaseException("Exception in setting vote to user or quote");
        }
    }

    /**
     * set vote to quote and user
     *
     * @param userId
     * @param quoteId
     * @param vote
     */
    private void setVoteToUserAndQuote(Long userId, Long quoteId, Vote vote) {
        String urlUser = HOST_NAME_USER + "setVote?userId=" + userId + "&voteId=" + vote.getId();
        String urlQuote = HOST_NAME_QUOTE + "setVote?quoteId=" + quoteId + "&voteId=" + vote.getId();

        try {
            restTemplate.put(urlUser, String.class);
            restTemplate.put(urlQuote, String.class);
        } catch (HttpClientErrorException e) {
            throw new DataBaseException("Exception in setting vote to user or quote");
        }
    }

    /**
     * get result grase to quote
     *
     * @param quoteId
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
     * @param quoteId
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
     *
     * @param longList
     */
    private void checkingExistingVotes(List<Long> longList) {
        if (longList.isEmpty()) {
            throw new NoSuchEntityException("There are not votes");
        }
    }


}
