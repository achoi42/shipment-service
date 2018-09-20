package com.solstice.model.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.PROPERTY)
public class ShipmentLineItem {

  private long lineItemId;
  private long lineItemOrderId;

  public ShipmentLineItem() {
  }

  public ShipmentLineItem(long lineItemId, long lineItemOrderId) {
    this.lineItemId = lineItemId;
    this.lineItemOrderId = lineItemOrderId;
  }

  public long getLineItemId() {
    return lineItemId;
  }

  public void setLineItemId(long lineItemId) {
    this.lineItemId = lineItemId;
  }

  public long getLineItemOrderId() {
    return lineItemOrderId;
  }

  public void setLineItemOrderId(long lineItemOrderId) {
    this.lineItemOrderId = lineItemOrderId;
  }
}
