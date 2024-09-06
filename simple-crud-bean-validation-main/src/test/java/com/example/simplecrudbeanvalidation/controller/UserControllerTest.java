package com.example.simplecrudbeanvalidation.controller;

import com.example.simplecrudbeanvalidation.model.User;
import com.example.simplecrudbeanvalidation.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createUser_validData_success() throws Exception {
        User newUser = new User(null, "John Doe", "john.doe@example.com", "p@ssW0rd!");
        User savedUser = new User(1L, newUser.getName(), newUser.getEmail(), newUser.getPassword());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.name").value(savedUser.getName()))
                .andExpect(jsonPath("$.email").value(savedUser.getEmail()))
                .andExpect(jsonPath("$.password").value(savedUser.getPassword()));
    }

    @Test
    public void createUser_invalidData_failure() throws Exception {
        User invalidUser = new User(null, "", "invalid-email", "123");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllUsers() throws Exception {
        User user1 = new User(1L, "John Doe", "john.doe@example.com", "p@ssW0rd!");
        User user2 = new User(2L, "Jane Doe", "jane.doe@example.com", "p@ssW0rd!");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[0].name").value(user1.getName()))
                .andExpect(jsonPath("$[0].email").value(user1.getEmail()))
                .andExpect(jsonPath("$[0].password").value(user1.getPassword()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()))
                .andExpect(jsonPath("$[1].name").value(user2.getName()))
                .andExpect(jsonPath("$[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$[1].password").value(user2.getPassword()));
    }

    @Test
    public void getUserById_found() throws Exception {
        User user = new User(1L, "John Doe", "john.doe@example.com", "p@ssW0rd!");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }

    @Test
    public void getUserById_notFound() throws Exception {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());
    }
}

