package com.example.userservice.model.mapper;

import com.example.userservice.model.dto.UserDto;
import com.example.userservice.model.entity.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(UserAccount userAccount);
}
