package com.example.demo.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CartControllerTest extends BaseTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  CartRepository cartRepository;
  @Autowired
  ItemRepository itemRepository;

  @Test
  @WithMockUser
  public void testAddTocart() throws Exception {
    createUser("will");
    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("will");
    request.setItemId(1L);
    request.setQuantity(20);

    this.mockMvc.perform(post("/api/cart/addToCart").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isOk())
        .andExpect(jsonPath("$.items").isArray()).andExpect(jsonPath("$.items", hasSize(20)));
  }

  @Test
  @WithMockUser
  public void testRemoveFromcart() throws Exception {
    addItemsToUserCart("will");

    ModifyCartRequest request = new ModifyCartRequest();
    request.setUsername("will");
    request.setItemId(2L);
    request.setQuantity(10);
    this.mockMvc.perform(post("/api/cart/removeFromCart").contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))).andExpect(status().isOk())
        .andExpect(jsonPath("$.items").isArray()).andExpect(jsonPath("$.items", hasSize(10)));
  }

  private void addItemsToUserCart(String username) {
    User user = createUser(username);
    Optional<Item> item = itemRepository.findById(2L);
    int quantity = 20;

    Cart cart = user.getCart();
    IntStream.range(0, quantity).forEach(i -> cart.addItem(item.get()));
    cartRepository.save(cart);
  }

}
