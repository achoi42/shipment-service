package com.solstice.repository;

import com.solstice.model.domain.Shipment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//@RepositoryRestResource(collectionResourceRel = "shipments", path = "shipments")
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

  List<Shipment> findByShipmentAccountIdOrderByDeliveryDateAsc(long accountId);
}
