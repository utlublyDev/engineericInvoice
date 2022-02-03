package qa.engineeric.invoice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import qa.engineeric.invoice.domain.Invoice;

/**
 * Spring Data MongoDB repository for the Invoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {}
