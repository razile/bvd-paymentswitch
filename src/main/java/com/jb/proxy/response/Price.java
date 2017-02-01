
package com.jb.proxy.response;

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
    "baseFare",
    "totalFare",
    "tax",
    "fee",
    "promotion",
    "paidValue"
})
public class Price {

    @JsonProperty("baseFare")
    private BaseFare baseFare;
    @JsonProperty("totalFare")
    private TotalFare totalFare;
    @JsonProperty("tax")
    private List<Tax> tax = null;
    @JsonProperty("fee")
    private List<Object> fee = null;
    @JsonProperty("promotion")
    private List<Object> promotion = null;
    @JsonProperty("paidValue")
    private PaidValue paidValue;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("baseFare")
    public BaseFare getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BaseFare baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("totalFare")
    public TotalFare getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(TotalFare totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("tax")
    public List<Tax> getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(List<Tax> tax) {
        this.tax = tax;
    }

    @JsonProperty("fee")
    public List<Object> getFee() {
        return fee;
    }

    @JsonProperty("fee")
    public void setFee(List<Object> fee) {
        this.fee = fee;
    }

    @JsonProperty("promotion")
    public List<Object> getPromotion() {
        return promotion;
    }

    @JsonProperty("promotion")
    public void setPromotion(List<Object> promotion) {
        this.promotion = promotion;
    }

    @JsonProperty("paidValue")
    public PaidValue getPaidValue() {
        return paidValue;
    }

    @JsonProperty("paidValue")
    public void setPaidValue(PaidValue paidValue) {
        this.paidValue = paidValue;
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
