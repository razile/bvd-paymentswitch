
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
    "travelerQuantity",
    "baseFare",
    "totalFare",
    "tax",
    "fee",
    "promotion",
    "travelerTypeRef",
    "fareAssociation"
})
public class TravelerPrice {

    @JsonProperty("travelerQuantity")
    private Integer travelerQuantity;
    @JsonProperty("baseFare")
    private BaseFare__ baseFare;
    @JsonProperty("totalFare")
    private TotalFare__ totalFare;
    @JsonProperty("tax")
    private List<Tax__> tax = null;
    @JsonProperty("fee")
    private List<Object> fee = null;
    @JsonProperty("promotion")
    private List<Object> promotion = null;
    @JsonProperty("travelerTypeRef")
    private TravelerTypeRef travelerTypeRef;
    @JsonProperty("fareAssociation")
    private List<FareAssociation> fareAssociation = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("travelerQuantity")
    public Integer getTravelerQuantity() {
        return travelerQuantity;
    }

    @JsonProperty("travelerQuantity")
    public void setTravelerQuantity(Integer travelerQuantity) {
        this.travelerQuantity = travelerQuantity;
    }

    @JsonProperty("baseFare")
    public BaseFare__ getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BaseFare__ baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("totalFare")
    public TotalFare__ getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(TotalFare__ totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("tax")
    public List<Tax__> getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(List<Tax__> tax) {
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

    @JsonProperty("travelerTypeRef")
    public TravelerTypeRef getTravelerTypeRef() {
        return travelerTypeRef;
    }

    @JsonProperty("travelerTypeRef")
    public void setTravelerTypeRef(TravelerTypeRef travelerTypeRef) {
        this.travelerTypeRef = travelerTypeRef;
    }

    @JsonProperty("fareAssociation")
    public List<FareAssociation> getFareAssociation() {
        return fareAssociation;
    }

    @JsonProperty("fareAssociation")
    public void setFareAssociation(List<FareAssociation> fareAssociation) {
        this.fareAssociation = fareAssociation;
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
