package qa.engineeric.invoice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import qa.engineeric.invoice.web.rest.TestUtil;

class ShipmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Shipment.class);
        Shipment shipment1 = new Shipment();
        shipment1.setId("id1");
        Shipment shipment2 = new Shipment();
        shipment2.setId(shipment1.getId());
        assertThat(shipment1).isEqualTo(shipment2);
        shipment2.setId("id2");
        assertThat(shipment1).isNotEqualTo(shipment2);
        shipment1.setId(null);
        assertThat(shipment1).isNotEqualTo(shipment2);
    }
}
