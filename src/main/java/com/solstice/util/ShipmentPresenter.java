package com.solstice.util;

import com.solstice.client.OrderClient;
import com.solstice.model.domain.Shipment;
import com.solstice.model.domain.ShipmentLineItem;
import com.solstice.model.info.OrderInfo;
import com.solstice.model.info.OrderLineItemInfo;
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
    List<OrderLineItemInfo> shipmentLineItems = orderClient.getLineItemsByShipment(shipment.getShipmentId(),shipment.getShipmentAccountId());
    logger.info("Fetched {} line items for shipment {}", shipmentLineItems.size(), shipment.getShipmentId());
//    if(shipmentLineItems.size() > 0) {
//      if(shipmentLineItems.stream().filter(i -> i.getLineItemOrderId() == ))
//    }


    int orderNum = -1;
    if(shipmentLineItems.isEmpty()) {
      return new ShipmentSummary(
          orderNum,
          shipment.getShippedDate(),
          shipment.getDeliveryDate(),
          Collections.emptyList()
      );
    }

    return new ShipmentSummary(
        orderClient.getOrderDetails(shipmentLineItems.get(0).getLineItemOrderId()).getContent().getOrderNum(),
        shipment.getShippedDate(),
        shipment.getDeliveryDate(),
        shipmentLineItems.stream().map(i -> lineItemPresenter.present(i)).collect(Collectors.toList())
    );
  }
}
