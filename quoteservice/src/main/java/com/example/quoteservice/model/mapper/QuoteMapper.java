package com.example.quoteservice.model.mapper;

import com.example.quoteservice.model.dto.QuoteDto;
import com.example.quoteservice.model.entity.Quote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Profile;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper(componentModel = "spring")
@Profile("dev")
public interface QuoteMapper {

    @Mapping(target = "linkOnAuthor", expression = "java(\"http://\" + userHost + \":8090/user/\"  +quote.getUserAccountId())")
    @Mapping(target = "linkOnVotes", expression = "java(\"http://\" + voteHost + \":8090/vote/quote/\" + quote.getId())")
    QuoteDto toQuoteDto(Quote quote, String userHost, String voteHost);

    default Timestamp map(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }
}
