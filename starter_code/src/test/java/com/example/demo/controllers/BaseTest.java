package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import junit.framework.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

abstract class BaseTest  extends TestCase {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @Autowired
  protected UserRepository userRepository;

  User createUser(String username) {
    User user = new User();
    user.setUsername(username);
    user.setPassword("MyPassword");
    Cart cart = new Cart();
    user.setCart(cart);
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return user;
  }

}
