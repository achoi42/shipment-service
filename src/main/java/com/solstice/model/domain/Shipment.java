package com.solstice.model.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Shipment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long shipmentId;
  @Column(nullable = false)
  private Long shipmentAccountId;
  @Column(nullable = false)
  private Long shipmentAddressId;
//  // Line item must have reference to order, as per requirements of order-service
////  @Tuplizer()
////  private ArrayList<Pair<Long,Long>> shipmentLineItems;
//  @ElementCollection
//  private List<ShipmentLineItem> shipmentLineItems = new ArrayList<>();
  private LocalDate shippedDate;
  private LocalDate deliveryDate;

  @Transient
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Shipment() {
  }

//  public Shipment(long shipmentAccountId, long shipmentAddressId,
//      ArrayList<ShipmentLineItem> shipmentLineItems, LocalDate shippedDate, LocalDate deliveryDate) {
//    this.shipmentAccountId = shipmentAccountId;
//    this.shipmentAddressId = shipmentAddressId;
//    if(shipmentLineItems == null) {
//      this.shipmentLineItems = new ArrayList<>(Collections.emptyList());
//    }
//    else {
//      this.shipmentLineItems = shipmentLineItems;
//    }
//    this.shippedDate = shippedDate;
//    this.deliveryDate = deliveryDate;
//  }

  @Autowired
  public Shipment(Long shipmentAccountId, Long shipmentAddressId, LocalDate shippedDate,
      LocalDate deliveryDate) {
    this.shipmentAccountId = shipmentAccountId;
    this.shipmentAddressId = shipmentAddressId;
    this.shippedDate = shippedDate;
    this.deliveryDate = deliveryDate;
  }

  public long getShipmentId() {
    return shipmentId;
  }

  public void setShipmentId(long shipmentId) {
    this.shipmentId = shipmentId;
  }

  public long getShipmentAccountId() {
    return shipmentAccountId;
  }

  public void setShipmentAccountId(long shipmentAccountId) {
    this.shipmentAccountId = shipmentAccountId;
  }

  public long getShipmentAddressId() {
    return shipmentAddressId;
  }

  public void setShipmentAddressId(long shipmentAddressId) {
    this.shipmentAddressId = shipmentAddressId;
  }
//
//  public List<ShipmentLineItem> getShipmentLineItems() {
//    return shipmentLineItems;
//  }
//
//  public void setShipmentLineItems(
//      List<ShipmentLineItem> shipmentLineItems) {
//    this.shipmentLineItems = shipmentLineItems;
//  }
//
//  public void addShipmentLineItem(ShipmentLineItem shipmentLineItem) {
//    if(shipmentLineItems.contains(shipmentLineItem)) {
//      logger.warn("Client attempting to add already associated line item {} to shipment {}", shipmentLineItem.getLineItemId(), shipmentId);
//      return;
//    }
//    shipmentLineItems.add(shipmentLineItem);
//  }
//
//  public void removeShipmentLineItem(ShipmentLineItem shipmentLineItem) {
//    if(!shipmentLineItems.contains(shipmentLineItem)) {
//      logger.warn("Client attempting to remove line item {} not associated with shipment {}", shipmentLineItem.getLineItemId(), shipmentId);
//      return;
//    }
//    shipmentLineItems.remove(shipmentLineItem);
//  }

  public LocalDate getShippedDate() {
    return shippedDate;
  }

  public void setShippedDate(LocalDate shippedDate) {
    this.shippedDate = shippedDate;
  }

  public LocalDate getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(LocalDate deliveryDate) {
    this.deliveryDate = deliveryDate;
  }
}
