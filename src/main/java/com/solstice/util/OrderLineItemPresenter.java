package com.solstice.util;

import com.solstice.client.OrderClient;
import com.solstice.client.ProductClient;
import com.solstice.model.info.OrderLineItemInfo;
import com.solstice.model.summary.OrderLineItemSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemPresenter {

  private OrderClient orderClient;
  private ProductClient productClient;

  protected OrderLineItemPresenter() {

  }

  @Autowired
  public OrderLineItemPresenter(OrderClient orderClient,
      ProductClient productClient) {
    this.orderClient = orderClient;
    this.productClient = productClient;
  }

  public OrderLineItemSummary present(OrderLineItemInfo lineItemInfo) {
//    int orderNum = orderClient.getOrderDetails(lineItemInfo.getLineItemOrderId()).getContent().getOrderNum();
    long productId = orderClient.readLineItem(lineItemInfo.getLineItemOrderId(),lineItemInfo.getLineItemId()).getContent().getLineItemProductId();
    String productName = productClient.getProduct(productId).getContent().getProductName();

    return new OrderLineItemSummary(
        productName,
        lineItemInfo.getQuantity()
    );
  }
}
