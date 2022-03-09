package qa.engineeric.invoice.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Payment.
 */
@Document(collection = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("customer_id")
    private String customerId;

    @Field("order_id")
    private String orderId;

    @Field("amount")
    private Double amount;

    @Field("store_owner_id")
    private String storeOwnerId;

    @Field("web_key")
    private String webKey;

    @Field("created_at")
    private ZonedDateTime createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Payment id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public Payment customerId(String customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public Payment orderId(String orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Payment amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStoreOwnerId() {
        return this.storeOwnerId;
    }

    public Payment storeOwnerId(String storeOwnerId) {
        this.setStoreOwnerId(storeOwnerId);
        return this;
    }

    public void setStoreOwnerId(String storeOwnerId) {
        this.storeOwnerId = storeOwnerId;
    }

    public String getWebKey() {
        return this.webKey;
    }

    public Payment webKey(String webKey) {
        this.setWebKey(webKey);
        return this;
    }

    public void setWebKey(String webKey) {
        this.webKey = webKey;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Payment createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", customerId='" + getCustomerId() + "'" +
            ", orderId='" + getOrderId() + "'" +
            ", amount=" + getAmount() +
            ", storeOwnerId='" + getStoreOwnerId() + "'" +
            ", webKey='" + getWebKey() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
