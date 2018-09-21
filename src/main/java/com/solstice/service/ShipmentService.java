package com.solstice.service;

import com.solstice.client.AccountClient;
import com.solstice.client.OrderClient;
import com.solstice.model.domain.Shipment;
import com.solstice.model.info.AddressInfo;
import com.solstice.repository.ShipmentRepository;
import com.solstice.util.BadRequestException;
import com.solstice.util.NotFoundException;
import java.util.List;
import java.util.Optional;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

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
    // Default accountId, fetch all
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

    if(!hasValidReferences(shipment)) {
      logger.error("Client attempted to create shipment with invalid references");
      throw new BadRequestException();
    }

    logger.info("Shipment has valid references, creating now");
    return shipmentRepository.save(shipment);
  }

  public Shipment updateShipment(Shipment toUpdate, long shipmentId) {
    checkId(shipmentRepository, shipmentId);
    if(toUpdate == null) {
      logger.error("Null request body when attempting to update shipment {}", shipmentId);
      throw new BadRequestException();
    }

    Shipment myShipment = shipmentRepository.getOne(shipmentId);
    logger.info("Updating shipment {}", myShipment.getShipmentId());

    if(shipmentId != toUpdate.getShipmentId()) {
      logger.error("Client attempted to update shipment id {} at endpoint for shipment {}", toUpdate.getShipmentId(), shipmentId);
      throw new BadRequestException();
    }

    if(hasValidReferences(toUpdate)) {
      logger.info("Shipment has valid references, creating now");
      myShipment.setShipmentAccountId(toUpdate.getShipmentAccountId());
      myShipment.setShipmentAddressId(toUpdate.getShipmentAddressId());
      myShipment.setShippedDate(toUpdate.getShippedDate());
      myShipment.setDeliveryDate(toUpdate.getDeliveryDate());
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
    Resource<AddressInfo> fetchedAddress = accountClient.readSingleAddress(shipment.getShipmentAccountId(), shipment.getShipmentAddressId());

    // If account-service returns valid response, then address and account references are valid
    if(fetchedAddress.getContent() == null) {
      logger.error("Shipment has invalid address {} at account {}", shipment.getShipmentAddressId(), shipment.getShipmentAccountId());
      return false;
    }
    return true;
  }
}
