package com.solstice.client;

import com.solstice.model.info.OrderInfo;
import com.solstice.model.info.LineItemInfo;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("order-service")
@Component
public interface OrderClient {

  @GetMapping("/orders")
  List<OrderInfo> getAllOrders(@RequestParam(value="accountId", required = false, defaultValue = "-1") long accountId);

  @GetMapping("/orders/{id}")
  Resource<OrderInfo> getOrderDetails(@PathVariable(value="id") long orderId);

  @GetMapping("/orders/lines/shipments/{shipmentId}")
  List<LineItemInfo> getLineItemsByShipment(@PathVariable(value="shipmentId") long shipmentId, @RequestParam(value="accountId", required = false, defaultValue = "-1") long accountId);

  @GetMapping("/orders/{orderId}/lines/{lineId}")
  Resource<LineItemInfo> readLineItem(@PathVariable(value="orderId") long orderId, @PathVariable(name="lineId") long lineId);

  @PutMapping("/orders/{orderId}/lines/{lineId}")
  Resource<LineItemInfo> updateLineItem(
      @PathVariable(name="orderId") long orderId,
      @PathVariable(name="lineId") long lineId,
      @RequestBody OrderLineItemRequestBody lineItem
  );
}
