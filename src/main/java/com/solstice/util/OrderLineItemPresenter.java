package com.solstice.util;

import com.solstice.model.info.OrderInfoLineItem;
import com.solstice.model.summary.OrderLineItemSummary;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemPresenter {

  protected OrderLineItemPresenter() {
  }

  public OrderLineItemSummary present(OrderInfoLineItem lineItemInfo) {
    return new OrderLineItemSummary(
        lineItemInfo.getProductName(),
        lineItemInfo.getQuantity()
    );
  }
}
