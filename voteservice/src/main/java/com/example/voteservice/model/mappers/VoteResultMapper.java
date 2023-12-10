package com.example.voteservice.model.mappers;

import com.example.voteservice.model.dto.VoteResultDto;
import com.example.voteservice.model.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;

@Mapper(componentModel = "spring")
public interface VoteResultMapper {
    @Mapping(target = "result", source = "result")
    VoteResultDto voteDto(Vote vote, int result);
    default Timestamp map(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }
}
