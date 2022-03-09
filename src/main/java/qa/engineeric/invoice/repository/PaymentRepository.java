package qa.engineeric.invoice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import qa.engineeric.invoice.domain.Payment;

/**
 * Spring Data MongoDB repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {}
