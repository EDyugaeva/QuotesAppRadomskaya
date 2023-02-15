package com.example.quoteservice.repository;

import com.example.quoteservice.model.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotesRepository extends JpaRepository<Quote, Long> {


}
