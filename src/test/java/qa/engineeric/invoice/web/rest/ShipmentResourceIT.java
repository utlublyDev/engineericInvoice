package qa.engineeric.invoice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import qa.engineeric.invoice.IntegrationTest;
import qa.engineeric.invoice.domain.Invoice;
import qa.engineeric.invoice.domain.Shipment;
import qa.engineeric.invoice.repository.ShipmentRepository;

/**
 * Integration tests for the {@link ShipmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentResourceIT {

    private static final String DEFAULT_USER_STORE_OWNER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_STORE_OWNER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TRACKING_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shipments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private MockMvc restShipmentMockMvc;

    private Shipment shipment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipment createEntity() {
        Shipment shipment = new Shipment()
            .userStoreOwnerId(DEFAULT_USER_STORE_OWNER_ID)
            .userId(DEFAULT_USER_ID)
            .trackingCode(DEFAULT_TRACKING_CODE)
            .date(DEFAULT_DATE)
            .details(DEFAULT_DETAILS);
        // Add required entity
        Invoice invoice;
        invoice = InvoiceResourceIT.createEntity();
        invoice.setId("fixed-id-for-tests");
        shipment.setInvoice(invoice);
        return shipment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Shipment createUpdatedEntity() {
        Shipment shipment = new Shipment()
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .trackingCode(UPDATED_TRACKING_CODE)
            .date(UPDATED_DATE)
            .details(UPDATED_DETAILS);
        // Add required entity
        Invoice invoice;
        invoice = InvoiceResourceIT.createUpdatedEntity();
        invoice.setId("fixed-id-for-tests");
        shipment.setInvoice(invoice);
        return shipment;
    }

    @BeforeEach
    public void initTest() {
        shipmentRepository.deleteAll();
        shipment = createEntity();
    }

    @Test
    void createShipment() throws Exception {
        int databaseSizeBeforeCreate = shipmentRepository.findAll().size();
        // Create the Shipment
        restShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isCreated());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeCreate + 1);
        Shipment testShipment = shipmentList.get(shipmentList.size() - 1);
        assertThat(testShipment.getUserStoreOwnerId()).isEqualTo(DEFAULT_USER_STORE_OWNER_ID);
        assertThat(testShipment.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testShipment.getTrackingCode()).isEqualTo(DEFAULT_TRACKING_CODE);
        assertThat(testShipment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testShipment.getDetails()).isEqualTo(DEFAULT_DETAILS);
    }

    @Test
    void createShipmentWithExistingId() throws Exception {
        // Create the Shipment with an existing ID
        shipment.setId("existing_id");

        int databaseSizeBeforeCreate = shipmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUserStoreOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentRepository.findAll().size();
        // set the field null
        shipment.setUserStoreOwnerId(null);

        // Create the Shipment, which fails.

        restShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isBadRequest());

        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentRepository.findAll().size();
        // set the field null
        shipment.setUserId(null);

        // Create the Shipment, which fails.

        restShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isBadRequest());

        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = shipmentRepository.findAll().size();
        // set the field null
        shipment.setDate(null);

        // Create the Shipment, which fails.

        restShipmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isBadRequest());

        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllShipments() throws Exception {
        // Initialize the database
        shipmentRepository.save(shipment);

        // Get all the shipmentList
        restShipmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipment.getId())))
            .andExpect(jsonPath("$.[*].userStoreOwnerId").value(hasItem(DEFAULT_USER_STORE_OWNER_ID)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].trackingCode").value(hasItem(DEFAULT_TRACKING_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)));
    }

    @Test
    void getShipment() throws Exception {
        // Initialize the database
        shipmentRepository.save(shipment);

        // Get the shipment
        restShipmentMockMvc
            .perform(get(ENTITY_API_URL_ID, shipment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipment.getId()))
            .andExpect(jsonPath("$.userStoreOwnerId").value(DEFAULT_USER_STORE_OWNER_ID))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.trackingCode").value(DEFAULT_TRACKING_CODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS));
    }

    @Test
    void getNonExistingShipment() throws Exception {
        // Get the shipment
        restShipmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewShipment() throws Exception {
        // Initialize the database
        shipmentRepository.save(shipment);

        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();

        // Update the shipment
        Shipment updatedShipment = shipmentRepository.findById(shipment.getId()).get();
        updatedShipment
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .trackingCode(UPDATED_TRACKING_CODE)
            .date(UPDATED_DATE)
            .details(UPDATED_DETAILS);

        restShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShipment))
            )
            .andExpect(status().isOk());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
        Shipment testShipment = shipmentList.get(shipmentList.size() - 1);
        assertThat(testShipment.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testShipment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testShipment.getTrackingCode()).isEqualTo(UPDATED_TRACKING_CODE);
        assertThat(testShipment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testShipment.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    void putNonExistingShipment() throws Exception {
        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();
        shipment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchShipment() throws Exception {
        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();
        shipment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamShipment() throws Exception {
        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();
        shipment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateShipmentWithPatch() throws Exception {
        // Initialize the database
        shipmentRepository.save(shipment);

        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();

        // Update the shipment using partial update
        Shipment partialUpdatedShipment = new Shipment();
        partialUpdatedShipment.setId(shipment.getId());

        partialUpdatedShipment.userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID).userId(UPDATED_USER_ID).date(UPDATED_DATE);

        restShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShipment))
            )
            .andExpect(status().isOk());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
        Shipment testShipment = shipmentList.get(shipmentList.size() - 1);
        assertThat(testShipment.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testShipment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testShipment.getTrackingCode()).isEqualTo(DEFAULT_TRACKING_CODE);
        assertThat(testShipment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testShipment.getDetails()).isEqualTo(DEFAULT_DETAILS);
    }

    @Test
    void fullUpdateShipmentWithPatch() throws Exception {
        // Initialize the database
        shipmentRepository.save(shipment);

        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();

        // Update the shipment using partial update
        Shipment partialUpdatedShipment = new Shipment();
        partialUpdatedShipment.setId(shipment.getId());

        partialUpdatedShipment
            .userStoreOwnerId(UPDATED_USER_STORE_OWNER_ID)
            .userId(UPDATED_USER_ID)
            .trackingCode(UPDATED_TRACKING_CODE)
            .date(UPDATED_DATE)
            .details(UPDATED_DETAILS);

        restShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShipment))
            )
            .andExpect(status().isOk());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
        Shipment testShipment = shipmentList.get(shipmentList.size() - 1);
        assertThat(testShipment.getUserStoreOwnerId()).isEqualTo(UPDATED_USER_STORE_OWNER_ID);
        assertThat(testShipment.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testShipment.getTrackingCode()).isEqualTo(UPDATED_TRACKING_CODE);
        assertThat(testShipment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testShipment.getDetails()).isEqualTo(UPDATED_DETAILS);
    }

    @Test
    void patchNonExistingShipment() throws Exception {
        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();
        shipment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchShipment() throws Exception {
        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();
        shipment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamShipment() throws Exception {
        int databaseSizeBeforeUpdate = shipmentRepository.findAll().size();
        shipment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shipment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Shipment in the database
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteShipment() throws Exception {
        // Initialize the database
        shipmentRepository.save(shipment);

        int databaseSizeBeforeDelete = shipmentRepository.findAll().size();

        // Delete the shipment
        restShipmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Shipment> shipmentList = shipmentRepository.findAll();
        assertThat(shipmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
