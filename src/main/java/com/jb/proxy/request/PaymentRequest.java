
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
    "languageCode",
    "userSessionId",
    "notify",
    "pay",
    "customer",
    "pos"
})
public class PaymentRequest {

    @JsonProperty("languageCode")
    private String languageCode;
    @JsonProperty("userSessionId")
    private String userSessionId;
    @JsonProperty("notify")
    private Notify notify;
    @JsonProperty("pay")
    private List<Pay> pay = null;
    @JsonProperty("customer")
    private List<Customer> customer = null;
    @JsonProperty("pos")
    private String pos;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("languageCode")
    public String getLanguageCode() {
        return languageCode;
    }

    @JsonProperty("languageCode")
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @JsonProperty("userSessionId")
    public String getUserSessionId() {
        return userSessionId;
    }

    @JsonProperty("userSessionId")
    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }

    @JsonProperty("notify")
    public Notify getNotify() {
        return notify;
    }

    @JsonProperty("notify")
    public void setNotify(Notify notify) {
        this.notify = notify;
    }

    @JsonProperty("pay")
    public List<Pay> getPay() {
        return pay;
    }

    @JsonProperty("pay")
    public void setPay(List<Pay> pay) {
        this.pay = pay;
    }

    @JsonProperty("customer")
    public List<Customer> getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(List<Customer> customer) {
        this.customer = customer;
    }

    @JsonProperty("pos")
    public String getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(String pos) {
        this.pos = pos;
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
