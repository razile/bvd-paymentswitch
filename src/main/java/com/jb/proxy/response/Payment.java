
package com.jb.proxy.response;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "uri",
    "id",
    "status",
    "typeRef",
    "customerRef",
    "amount",
    "paymentCard"
})
public class Payment {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("typeRef")
    private TypeRef________ typeRef;
    @JsonProperty("customerRef")
    private CustomerRef customerRef;
    @JsonProperty("amount")
    private Amount amount;
    @JsonProperty("paymentCard")
    private PaymentCard paymentCard;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("typeRef")
    public TypeRef________ getTypeRef() {
        return typeRef;
    }

    @JsonProperty("typeRef")
    public void setTypeRef(TypeRef________ typeRef) {
        this.typeRef = typeRef;
    }

    @JsonProperty("customerRef")
    public CustomerRef getCustomerRef() {
        return customerRef;
    }

    @JsonProperty("customerRef")
    public void setCustomerRef(CustomerRef customerRef) {
        this.customerRef = customerRef;
    }

    @JsonProperty("amount")
    public Amount getAmount() {
        return amount;
    }

    @JsonProperty("amount")
    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    @JsonProperty("paymentCard")
    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    @JsonProperty("paymentCard")
    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
