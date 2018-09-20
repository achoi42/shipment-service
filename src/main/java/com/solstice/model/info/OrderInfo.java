package com.solstice.model.info;

import java.util.List;

public class OrderInfo {

  private int orderNum;
  private List<OrderLineItemInfo> lineItems;

  public OrderInfo() {

  }

  public OrderInfo(int orderNum, List<OrderLineItemInfo> lineItems) {
    this.orderNum = orderNum;
    this.lineItems = lineItems;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public List<OrderLineItemInfo> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<OrderLineItemInfo> lineItems) {
    this.lineItems = lineItems;
  }
}
