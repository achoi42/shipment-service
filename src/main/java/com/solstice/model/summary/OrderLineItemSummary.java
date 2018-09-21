package com.solstice.model.summary;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import org.springframework.beans.factory.annotation.Autowired;

@Embeddable
@Access(AccessType.PROPERTY)
public class OrderLineItemSummary {

  private String productName;
  private int quantity;

  public OrderLineItemSummary() {
  }

  @Autowired
  public OrderLineItemSummary(String productName, int quantity) {
    this.productName = productName;
    this.quantity = quantity;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
