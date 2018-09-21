package com.solstice.model.summary;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Embedded;
import org.springframework.beans.factory.annotation.Autowired;

public class ShipmentSummary {

  private int orderNum;
  private LocalDate shipmentDate;
  private LocalDate deliveryDate;

  @Embedded
  private List<OrderLineItemSummary> lineItems;

  public ShipmentSummary() {
  }

  @Autowired
  public ShipmentSummary(int orderNum, LocalDate shipmentDate, LocalDate deliveryDate,
      List<OrderLineItemSummary> lineItems) {
    this.orderNum = orderNum;
    this.shipmentDate = shipmentDate;
    this.deliveryDate = deliveryDate;
    this.lineItems = lineItems;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public LocalDate getShipmentDate() {
    return shipmentDate;
  }

  public void setShipmentDate(LocalDate shipmentDate) {
    this.shipmentDate = shipmentDate;
  }

  public LocalDate getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(LocalDate deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public List<OrderLineItemSummary> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<OrderLineItemSummary> lineItems) {
    this.lineItems = lineItems;
  }
}
