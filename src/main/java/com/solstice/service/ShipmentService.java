package com.solstice.service;

import com.solstice.client.AccountClient;
import com.solstice.client.OrderClient;
import com.solstice.client.OrderLineItemRequestBody;
import com.solstice.model.domain.Shipment;
import com.solstice.model.domain.ShipmentLineItem;
import com.solstice.model.info.AddressInfo;
import com.solstice.model.info.OrderLineItemInfo;
import com.solstice.repository.ShipmentRepository;
import com.solstice.util.BadRequestException;
import com.solstice.util.NotFoundException;
import java.util.List;
import java.util.Optional;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

  @Value("${id.default.null}")
  private long defaultNullId;

  private ShipmentRepository shipmentRepository;
  private AccountClient accountClient;
  private OrderClient orderClient;

  @Transient
  private Logger logger = LoggerFactory.getLogger(getClass());

  protected ShipmentService() {

  }

  @Autowired
  public ShipmentService(ShipmentRepository shipmentRepository,
      AccountClient accountClient, OrderClient orderClient) {
    this.shipmentRepository = shipmentRepository;
    this.accountClient = accountClient;
    this.orderClient = orderClient;
  }

  public List<Shipment> fetchAccountShipments(long accountId) {
    if(accountId == -1) {
      return shipmentRepository.findAll();
    }
      return shipmentRepository.findByShipmentAccountIdOrderByDeliveryDateAsc(accountId);
  }

  public Shipment addShipment(Shipment shipment) {
    if(shipment == null) {
      logger.error("Client attempted to create null shipment");
      throw new BadRequestException();
    }
//    if(shipmentRepository.findById(shipment.getShipmentId()).isPresent()) {
//      return updateShipment(shipment);
//    }

//    // Check Account Id
//    // Check Address Id
//    Resource<AddressInfo> fetchedAddress = accountClient.readSingleAddress(shipment.getShipmentAccountId(), shipment.getShipmentAddressId());
//    if(fetchedAddress.getContent() == null) {
//      logger.error("Shipment has invalid address {} at account {}", shipment.getShipmentAddressId(), shipment.getShipmentAccountId());
//      throw new BadRequestException();
//    }
//
//    // Check Order Id
//    // Check LineItem Id
//    if(shipment.getShipmentLineItems().size() != 0) {
//      List<Resource<OrderLineItemInfo>> fetchedLineItems = shipment.getShipmentLineItems().stream().map(i -> new Resource<>(orderClient.readLineItem(i.getValue1(),i.getValue0()).getContent())).collect(Collectors.toList());
//      for(int i = 0; i < fetchedLineItems.size(); i++) {
//        if(fetchedLineItems.get(i).getContent() == null) {
//          logger.error("Shipment has invalid line item {} for order {}", shipment.getShipmentLineItems().get(i).getValue0(), shipment.getShipmentLineItems().get(i).getValue1());
//          throw new BadRequestException();
//        }
//      }
//    }
    if(hasValidReferences(shipment)) {
      Shipment saved = shipmentRepository.save(shipment);
      return saved;
    }
    else {
      throw new BadRequestException();
    }
  }

  public Shipment updateShipment(Shipment toUpdate, long shipmentId) {
    checkId(shipmentRepository, shipmentId);
    if(toUpdate == null) {
      logger.error("Null request body when attempting to update shipment {}", shipmentId);
      throw new BadRequestException();
    }

    Shipment myShipment = shipmentRepository.getOne(shipmentId);

    if(shipmentId != toUpdate.getShipmentId()) {
      logger.error("Client attempted to update shipment id {} at endpoint for shipment {}", toUpdate.getShipmentId(), shipmentId);
      throw new BadRequestException();
    }

    if(hasValidReferences(toUpdate)) {
      myShipment.setShipmentAccountId(toUpdate.getShipmentAccountId());
      myShipment.setShipmentAddressId(toUpdate.getShipmentAddressId());
      myShipment.setShippedDate(toUpdate.getShippedDate());
      myShipment.setDeliveryDate(toUpdate.getDeliveryDate());
//      myShipment.setShipmentLineItems(updateShipmentLineItemReferences(myShipment.getShipmentLineItems(),toUpdate.getShipmentLineItems(),shipmentId));
    }

    return shipmentRepository.save(myShipment);
  }

  private void checkId(JpaRepository repository, long id) {
    Optional checker = repository.findById(id);
    if(!checker.isPresent()) {
      logger.error("Entity with id " + id + " not found");
      throw new NotFoundException();
    }
  }

  private boolean hasValidReferences(Shipment shipment) {
    // If account-service returns valid response, then address and account references are valid
    Resource<AddressInfo> fetchedAddress = accountClient.readSingleAddress(shipment.getShipmentAccountId(), shipment.getShipmentAddressId());
    if(fetchedAddress.getContent() == null) {
      logger.error("Shipment has invalid address {} at account {}", shipment.getShipmentAddressId(), shipment.getShipmentAccountId());
      return false;
    }

    // If order-service returns valid response, then line item and order references are valid
//    if(shipment.getShipmentLineItems().size() != 0) {
//      logger.info("Validating {} line item references", shipment.getShipmentLineItems().size());
//      List<Resource<OrderLineItemInfo>> fetchedLineItems = shipment.getShipmentLineItems().stream().map(i -> new Resource<>(orderClient.readLineItem(i.getLineItemOrderId(),i.getLineItemId()).getContent())).collect(Collectors.toList());
//      for(int i = 0; i < fetchedLineItems.size(); i++) {
//        if(fetchedLineItems.get(i).getContent() == null) {
//          logger.error("Shipment has invalid line item {} for order {}", shipment.getShipmentLineItems().get(i).getLineItemId(), shipment.getShipmentLineItems().get(i).getLineItemOrderId());
//          return false;
//        }
//      }
//    }
    return true;
  }

  private List<ShipmentLineItem> updateShipmentLineItemReferences(List<ShipmentLineItem> existing, List<ShipmentLineItem> toUpdate, long shipmentId) {
    OrderLineItemInfo existingLineItemInfo;
    // Disassociate exiting line items that are not in updated line item list
    for(ShipmentLineItem o : existing) {
      if(toUpdate.contains(o)) {
        continue;
      }
      else {
        existingLineItemInfo = orderClient.readLineItem(o.getLineItemOrderId(), o.getLineItemId()).getContent();
        logger.info("Existing order line item {} for order {} disassociated with shipment {} during shipment update", o.getLineItemId(), o.getLineItemOrderId(), existingLineItemInfo.getLineItemShipmentId());
        existing.remove(o);
        orderClient.updateLineItem(
            existingLineItemInfo.getLineItemOrderId(),
            existingLineItemInfo.getLineItemId(),
            new OrderLineItemRequestBody(
                existingLineItemInfo.getLineItemProductId(),
                existingLineItemInfo.getQuantity(),
                existingLineItemInfo.getLineItemPrice(),
                defaultNullId
            )
        );
      }
    }

    // Associate shipmentId with any new line items during shipment update
    for(ShipmentLineItem u : toUpdate) {
      if(existing.contains(u)) {
        continue;
      }
      else {
        existingLineItemInfo = orderClient.readLineItem(u.getLineItemOrderId(), u.getLineItemId()).getContent();
        logger.info("Associating shipment {} with line item {} for order {}");
        existing.add(u);
        orderClient.updateLineItem(
            existingLineItemInfo.getLineItemOrderId(),
            existingLineItemInfo.getLineItemId(),
            new OrderLineItemRequestBody(
                existingLineItemInfo.getLineItemProductId(),
                existingLineItemInfo.getQuantity(),
                existingLineItemInfo.getLineItemPrice(),
                shipmentId
            )
        );
      }
    }

    if(!existing.containsAll(toUpdate) && !toUpdate.containsAll(existing)) {
      logger.error("Resulting list of line item references does not match expected");
    }

    return existing;
  }
}
