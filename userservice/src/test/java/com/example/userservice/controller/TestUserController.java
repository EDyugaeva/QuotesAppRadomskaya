package com.example.userservice.controller;

import com.example.userservice.model.entity.UserAccount;
import com.example.userservice.model.exceptions.NoSuchEntityException;
import com.example.userservice.model.mapper.UserMapperImpl;
import com.example.userservice.repository.UserAccountRepository;
import com.example.userservice.services.impl.UserAccountServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static com.example.userservice.controller.Constants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class TestUserController {
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private UserAccountServiceImpl userAccountService;

    @MockBean
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserController userController;

    @SpyBean
    private UserMapperImpl userMapper;



    @Test
    public void contextLoads() {
        assertThat(userController).isNotNull();
    }

    @Before("")
    public void startDate() {

    }


    @Test
    public void testAddUser() {
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(USER_ACCOUNT);
        when(userAccountRepository.findUserAccountByEmail(EMAIL)).thenReturn(Optional.empty());
        String url = URL + PORT + "/user?name=" + NAME + "&email=" + EMAIL + "&password=" + PASSWORD;
        System.out.println(url);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(NAME))
                    .andExpect(jsonPath("$.email").value(EMAIL));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddUserWithExistingEmail() {
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(USER_ACCOUNT);
        when(userAccountRepository.findUserAccountByEmail(EMAIL)).thenReturn(Optional.of(USER_ACCOUNT));
        String url = URL + PORT + "/user?name=" + NAME + "&email=" + EMAIL + "&password=" + PASSWORD;
        System.out.println(url);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetUser() {
        when(userAccountRepository.findById(ID)).thenReturn(Optional.of(USER_ACCOUNT));
        String url = URL + PORT + "/user/" + ID;
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(NAME))
                    .andExpect(jsonPath("$.email").value(EMAIL));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(userAccountRepository.findById(ID)).thenThrow(NoSuchEntityException.class);
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .get(url)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
