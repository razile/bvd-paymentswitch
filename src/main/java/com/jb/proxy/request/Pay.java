
package com.jb.proxy.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "paymentCard",
    "customerRef",
    "type",
    "itemAllocation"
})
public class Pay {

    @JsonProperty("paymentCard")
    private PaymentCard paymentCard;
    @JsonProperty("customerRef")
    private CustomerRef customerRef;
    @JsonProperty("type")
    private String type;
    @JsonProperty("itemAllocation")
    private List<ItemAllocation> itemAllocation = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("paymentCard")
    public PaymentCard getPaymentCard() {
        return paymentCard;
    }

    @JsonProperty("paymentCard")
    public void setPaymentCard(PaymentCard paymentCard) {
        this.paymentCard = paymentCard;
    }

    @JsonProperty("customerRef")
    public CustomerRef getCustomerRef() {
        return customerRef;
    }

    @JsonProperty("customerRef")
    public void setCustomerRef(CustomerRef customerRef) {
        this.customerRef = customerRef;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("itemAllocation")
    public List<ItemAllocation> getItemAllocation() {
        return itemAllocation;
    }

    @JsonProperty("itemAllocation")
    public void setItemAllocation(List<ItemAllocation> itemAllocation) {
        this.itemAllocation = itemAllocation;
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
