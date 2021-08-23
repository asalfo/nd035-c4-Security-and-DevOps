package com.example.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest extends BaseTest {

  @Autowired
  private MockMvc mockMvc;


  @Test
  @WithMockUser()
  public void testFindById() throws Exception {
    User user = createUser("John");
    this.mockMvc.perform(get("/api/user/id/"+user.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser()
  public void testFindByUserName() throws Exception {
    User user = createUser("JOe");
    this.mockMvc.perform(get("/api/user/"+user.getUsername()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void testCreateUser() throws Exception {
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("Salif");
    userRequest.setPassword("MyPassword");
    userRequest.setConfirmPassword("MyPassword");

    this.mockMvc.perform(post("/api/user/create").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userRequest)))
        .andExpect(status().isOk());
  }

  @Test
  public void createUserWithInvalidPassword() throws Exception {
    CreateUserRequest userRequest = new CreateUserRequest();
    userRequest.setUsername("Salif");
    userRequest.setPassword("MyPass");
    userRequest.setConfirmPassword("MyPasssd");
    String requestBody;

    requestBody = new ObjectMapper().writeValueAsString(userRequest);

    this.mockMvc.perform(post("/api/user/create").contentType(MediaType.APPLICATION_JSON).content(requestBody))
        .andExpect(status().isBadRequest());
  }

}
