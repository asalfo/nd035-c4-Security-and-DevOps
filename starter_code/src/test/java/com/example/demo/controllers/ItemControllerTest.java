package com.example.demo.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.AddItemRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import javax.transaction.TransactionScoped;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ItemControllerTest extends TestCase {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ItemRepository itemRepository;

  @Test
  @WithMockUser()
  public void testGetItems() throws Exception {
    this.mockMvc.perform(get("/api/item").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser()
  public void testAddItem() throws Exception {

    AddItemRequest request = new AddItemRequest();
    request.setName("Coca Cola");
    request.setDescription("Origin flavour");
    request.setPrice(new BigDecimal("0.99"));

    this.mockMvc.perform(post("/api/item").contentType(MediaType.APPLICATION_JSON)
            .content( new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser()
  public void testGetItemById() throws Exception {

    this.mockMvc.perform(get("/api/item/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser()
  public void testGetItemsByName() throws Exception {
    this.mockMvc.perform(get("/api/item/name/Square Widget").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

}
