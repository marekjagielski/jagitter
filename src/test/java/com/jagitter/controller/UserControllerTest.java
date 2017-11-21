package com.jagitter.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.jagitter.Application;
import com.jagitter.data.User;
import com.jagitter.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Create a new user")
    public void testCreateUser1() throws Exception {
        String userId = UUID.randomUUID().toString();
        when(userRepository.findOne(userId)).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(new User(userId));
        mockMvc.perform(put("/users/" + userId))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Try to create user for existing user id")
    public void testCreateUser2() throws Exception {
        String userId = UUID.randomUUID().toString();
        when(userRepository.findOne(userId)).thenReturn(new User(userId));
        mockMvc.perform(put("/users/" + userId))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message", containsString(userId)))
                .andExpect(jsonPath("$.message", containsString("exists")));
    }

    @Test
    @DisplayName("Delete user with id")
    public void testDeleteUser1() throws Exception {
        String userId = UUID.randomUUID().toString();
        mockMvc.perform(delete("/users/" + userId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("List is empty")
    public void listUsers1() throws Exception {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/users"))
                .andExpect(status().isNoContent());
    }
}
