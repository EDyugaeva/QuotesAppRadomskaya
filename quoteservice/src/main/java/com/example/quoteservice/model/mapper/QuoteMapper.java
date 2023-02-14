package com.example.quoteservice.model.mapper;

import com.example.quoteservice.model.dto.QuoteDto;
import com.example.quoteservice.model.entity.Quote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper(componentModel = "spring")
public interface QuoteMapper {
    //TODO change localhost to constant
    //TODO votes link
    @Mapping(target = "linkOnAuthor", expression = "java(\"http://localhost:8081/user/?id=\" + quote.getUserAccountId())")
    @Mapping(target = "linkOnVotes", expression = "java(\"http://localhost:8083/vote/?quoteiId=\" + quote.getId())")
    QuoteDto toQuoteDto(Quote quote);

    default Timestamp map(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }
}