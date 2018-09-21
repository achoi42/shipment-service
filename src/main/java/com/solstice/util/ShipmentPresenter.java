package com.solstice.util;

import com.solstice.client.OrderClient;
import com.solstice.model.domain.Shipment;
import com.solstice.model.info.OrderInfo;
import com.solstice.model.info.LineItemInfo;
import com.solstice.model.info.OrderInfoLineItem;
import com.solstice.model.summary.OrderLineItemSummary;
import com.solstice.model.summary.ShipmentSummary;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentPresenter {

  private OrderClient orderClient;
  private OrderLineItemPresenter lineItemPresenter;

  private Logger logger = LoggerFactory.getLogger(getClass());

  public ShipmentPresenter() {
  }

  @Autowired
  public ShipmentPresenter(OrderClient orderClient,
      OrderLineItemPresenter lineItemPresenter) {
    this.orderClient = orderClient;
    this.lineItemPresenter = lineItemPresenter;
  }

  public ShipmentSummary present(Shipment shipment) {
    List<LineItemInfo> shipmentLineItems = orderClient.getLineItemsByShipment(shipment.getShipmentId(),shipment.getShipmentAccountId());
    logger.info("Fetched {} line items for shipment {}", shipmentLineItems.size(), shipment.getShipmentId());

    int orderNum = -1;
    if(shipmentLineItems.isEmpty()) {
      logger.info("Shipment has no line items");
      return new ShipmentSummary(
          orderNum,
          shipment.getShippedDate(),
          shipment.getDeliveryDate(),
          Collections.emptyList()
      );
    }

    OrderInfo orderInfo = orderClient.getOrderDetails(shipmentLineItems.get(0).getLineItemOrderId()).getContent();
    orderNum = orderInfo.getOrderNum();
    logger.info("Fetched order number {}", orderNum);
    List<OrderInfoLineItem> lineItemSummary = orderInfo.getLineItems().stream().filter(i -> i.getLineItemShipmentId() == shipment.getShipmentId()).collect(Collectors.toList());
    List<OrderLineItemSummary> shipmentLineItemSummary = lineItemSummary.stream().map(i -> lineItemPresenter.present(i)).collect(Collectors.toList());

    return new ShipmentSummary(
        orderNum,
        shipment.getShippedDate(),
        shipment.getDeliveryDate(),
        shipmentLineItemSummary
    );
  }
}
