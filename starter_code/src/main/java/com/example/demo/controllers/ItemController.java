package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.requests.AddItemRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/item")
public class ItemController {

  @Autowired
  private ItemRepository itemRepository;

  @GetMapping
  public ResponseEntity<List<Item>> getItems() {
    return ResponseEntity.ok(itemRepository.findAll());
  }

  @PostMapping()
  public ResponseEntity<Item> addItem(@RequestBody AddItemRequest request) {
    Item item = new Item();
    item.setName(request.getName());
    item.setDescription(request.getDescription());
    item.setPrice(request.getPrice());
    itemRepository.save(item);
    return ResponseEntity.ok(item);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Item> getItemById(@PathVariable Long id) {
    return ResponseEntity.of(itemRepository.findById(id));
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
    List<Item> items = itemRepository.findByName(name);
    return items == null || items.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(items);
  }

}
