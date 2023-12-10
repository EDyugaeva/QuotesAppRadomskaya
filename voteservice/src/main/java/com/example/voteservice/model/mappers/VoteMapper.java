package com.example.voteservice.model.mappers;

import com.example.voteservice.model.dto.VoteDto;
import com.example.voteservice.model.entity.Vote;
import org.mapstruct.Mapper;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper(componentModel = "spring")
public interface VoteMapper {
    VoteDto toVoteDto(Vote vote);
    default Timestamp map(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }
}
