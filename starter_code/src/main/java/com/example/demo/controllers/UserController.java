package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import java.security.SecureRandom;
import java.util.Optional;
import org.apache.juli.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final Logger log = LoggerFactory.getLogger(UserController.class);
  private final UserRepository userRepository;

  private final CartRepository cartRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;


  public UserController(UserRepository userRepository, CartRepository cartRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.cartRepository = cartRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
     Optional<User> user = userRepository.findById(id);
     if(user.isEmpty()) {
       log.error(String.format("User with id [%s] not found", id));
     }
    return ResponseEntity.of(user);
  }

  @GetMapping("/{username}")
  public ResponseEntity<User> findByUserName(@PathVariable String username) {
    User user = userRepository.findByUsername(username);
    if(user == null) {
      log.error(String.format("Username [%s] not found", username));
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(user);
  }

  @PostMapping("/create")
  public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
    User user = new User();
    user.setUsername(createUserRequest.getUsername());
    Cart cart = new Cart();
    cartRepository.save(cart);
    user.setCart(cart);
    if (createUserRequest.getPassword().length() < 7 || !createUserRequest.getPassword()
        .equals(createUserRequest.getConfirmPassword())) {
      log.error("Error: invalid password");
      return ResponseEntity.badRequest().build();
    }
    user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
    try {
      userRepository.save(user);
      log.info("Success: user created");
    } catch (Exception exception) {
      log.error(exception.getMessage(),exception.getCause());
      return ResponseEntity.badRequest().build();
    }

    return ResponseEntity.ok(user);
  }

}
