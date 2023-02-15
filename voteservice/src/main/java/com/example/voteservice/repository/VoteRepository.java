package com.example.voteservice.repository;


import com.example.voteservice.model.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query(value = "SELECT * FROM vote WHERE quote = ? and author = ?", nativeQuery = true)
    Optional<Vote> getVoteBy(Long quoteId, Long userId);


    @Query(value = "SELECT SUM(grade) FROM vote WHERE quote = ?", nativeQuery = true)
    Optional<Integer> getGradeByQuote(Long quoteId);

    @Query(value = "select quote from vote group by quote order by sum(grade) desc limit 10;", nativeQuery = true)
    List<Long> findTopTenQuery();

    @Query(value = "select quote from vote group by quote order by sum(grade) limit 10;", nativeQuery = true)
    List<Long> findWorseTenQuery();

    @Query(value = "SELECT SUM(grade) FROM vote WHERE quote = ? and date_of_creation = ?", nativeQuery = true)
    Integer getGradeByQuoteAndDate(Long quoteId, LocalDate date);

    @Query(value = "SELECT  MIN(date_of_creation) from vote where quote = ?", nativeQuery = true)
    LocalDate findTheFirstDateOfVotesFromQuoteWithId(Long id);

    List<Vote> findVoteByQuote(Long quoteId);


}
