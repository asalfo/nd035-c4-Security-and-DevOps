package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class AddItemRequest {

  @JsonProperty
  private String name;
  @JsonProperty
  private String description;
  @JsonProperty
  private BigDecimal price;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

}
