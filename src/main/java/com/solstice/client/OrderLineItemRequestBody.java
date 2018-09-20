package com.solstice.client;

public class OrderLineItemRequestBody {
  private long lineItemProductId;
  private int quantity;
  private double lineItemPrice;
  private long lineItemShipmentId;

  public OrderLineItemRequestBody() {
  }

  public OrderLineItemRequestBody(long lineItemProductId, int quantity, double lineItemPrice,
      long lineItemShipmentId) {
    this.lineItemProductId = lineItemProductId;
    this.quantity = quantity;
    this.lineItemPrice = lineItemPrice;
    this.lineItemShipmentId = lineItemShipmentId;
  }

  public long getLineItemProductId() {
    return lineItemProductId;
  }

  public void setLineItemProductId(long lineItemProductId) {
    this.lineItemProductId = lineItemProductId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public double getLineItemPrice() {
    return lineItemPrice;
  }

  public void setLineItemPrice(double lineItemPrice) {
    this.lineItemPrice = lineItemPrice;
  }

  public long getLineItemShipmentId() {
    return lineItemShipmentId;
  }

  public void setLineItemShipmentId(long lineItemShipmentId) {
    this.lineItemShipmentId = lineItemShipmentId;
  }
}
