
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
    "promotion"
})
public class TotalPrice {

    @JsonProperty("baseFare")
    private BaseFare_ baseFare;
    @JsonProperty("totalFare")
    private TotalFare_ totalFare;
    @JsonProperty("tax")
    private List<Tax_> tax = null;
    @JsonProperty("fee")
    private List<Object> fee = null;
    @JsonProperty("promotion")
    private List<Object> promotion = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("baseFare")
    public BaseFare_ getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BaseFare_ baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("totalFare")
    public TotalFare_ getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(TotalFare_ totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("tax")
    public List<Tax_> getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(List<Tax_> tax) {
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
