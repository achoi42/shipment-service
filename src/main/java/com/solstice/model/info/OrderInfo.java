package com.solstice.model.info;

import java.util.List;

public class OrderInfo {

  private int orderNum;
  private List<OrderInfoLineItem> lineItems;

  public OrderInfo() {

  }

  public OrderInfo(int orderNum, List<OrderInfoLineItem> lineItems) {
    this.orderNum = orderNum;
    this.lineItems = lineItems;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public List<OrderInfoLineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<OrderInfoLineItem> lineItems) {
    this.lineItems = lineItems;
  }
}
