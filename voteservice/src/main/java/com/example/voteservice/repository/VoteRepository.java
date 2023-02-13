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

    @Query(value = "SELECT * FROM vote WHERE quote_id = ? and user_account_id = ?", nativeQuery = true)
    Optional<Vote> getVoteBy(Long quoteId, Long userId);

    Optional<Vote> findFirstByQuoteId(Long id);

    @Query(value = "SELECT SUM(grade) FROM vote WHERE quote_id = ?", nativeQuery = true)
    int getGradeByQuote(Long quoteId);

    @Query(value = "SELECT user_account_id FROM vote WHERE quote_id = ?", nativeQuery = true)
    List<Long> findUserAccoundId(Long quoteId);

    @Query(value = "select quote_id from vote group by quote_id order by sum(grade) desc limit 10;", nativeQuery = true)
    List<Long> findTopTenQuery();

    @Query(value = "select quote_id from vote group by quote_id order by sum(grade) limit 10;", nativeQuery = true)
    List<Long> findWorseTenQuery();

    @Query(value = "SELECT SUM(grade) FROM vote WHERE quote_id = ? and date_of_creation = ?", nativeQuery = true)
    Integer getGradeByQuoteAndDate(Long quoteId, LocalDate date);

    @Query(value = "SELECT  MIN(date_of_creation) from vote where quote_id = ?", nativeQuery = true)
    LocalDate findTheFirstDateOfVotesFromQuoteWithId(Long id);

    Optional<Vote> findVoteById(Long id);
}
