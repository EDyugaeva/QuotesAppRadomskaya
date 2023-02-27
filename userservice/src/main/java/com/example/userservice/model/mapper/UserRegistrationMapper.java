package com.example.userservice.model.mapper;

import com.example.userservice.model.dto.UserRegistrationDto;
import com.example.userservice.model.entity.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRegistrationMapper {
    UserAccount toUser(UserRegistrationDto registrationDto);

}
