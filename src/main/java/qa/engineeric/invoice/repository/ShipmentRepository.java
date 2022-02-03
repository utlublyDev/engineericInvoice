package qa.engineeric.invoice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import qa.engineeric.invoice.domain.Shipment;

/**
 * Spring Data MongoDB repository for the Shipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentRepository extends MongoRepository<Shipment, String> {}
