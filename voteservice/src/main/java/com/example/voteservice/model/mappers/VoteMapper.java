package com.example.voteservice.model.mappers;

import com.example.voteservice.model.dto.VoteDto;
import com.example.voteservice.model.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    @Mapping(target = "author", source = "author")
    VoteDto voteDto(Vote vote);
    @Mapping(target = "author", source = "author")
    Vote toVote(VoteDto voteDto);

    default Timestamp map(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }
}
