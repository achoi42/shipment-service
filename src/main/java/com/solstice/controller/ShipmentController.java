package com.solstice.controller;

import com.solstice.model.domain.Shipment;
import com.solstice.model.summary.ShipmentSummary;
import com.solstice.service.ShipmentService;
import com.solstice.util.ShipmentPresenter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShipmentController {

  private ShipmentService shipmentService;
  private ShipmentPresenter shipmentPresenter;

  protected ShipmentController() {
  }

  @Autowired
  public ShipmentController(ShipmentService shipmentService,
      ShipmentPresenter shipmentPresenter) {
    this.shipmentService = shipmentService;
    this.shipmentPresenter = shipmentPresenter;
  }

  @GetMapping("/shipments")
  public List<ShipmentSummary> getAccountShipment(@RequestParam(value="accountId", required = false, defaultValue = "-1") long accountId) {
    return shipmentService.fetchAccountShipments(accountId).stream().map(s-> shipmentPresenter.present(s)).collect(Collectors.toList());
  }

  @PostMapping("/shipments")
  public Resource<Shipment> createAccountShipment(@RequestBody Shipment shipment) {
    return new Resource<> (shipmentService.addShipment(shipment));
  }
}
