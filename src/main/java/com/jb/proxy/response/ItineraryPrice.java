
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
    "uri",
    "id",
    "totalPrice",
    "travelerPrice",
    "farePrice",
    "itineraryRef",
    "ancillaryRef",
    "miscRef"
})
public class ItineraryPrice {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("totalPrice")
    private TotalPrice totalPrice;
    @JsonProperty("travelerPrice")
    private List<TravelerPrice> travelerPrice = null;
    @JsonProperty("farePrice")
    private List<FarePrice> farePrice = null;
    @JsonProperty("itineraryRef")
    private ItineraryRef itineraryRef;
    @JsonProperty("ancillaryRef")
    private List<Object> ancillaryRef = null;
    @JsonProperty("miscRef")
    private List<Object> miscRef = null;
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

    @JsonProperty("totalPrice")
    public TotalPrice getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("totalPrice")
    public void setTotalPrice(TotalPrice totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonProperty("travelerPrice")
    public List<TravelerPrice> getTravelerPrice() {
        return travelerPrice;
    }

    @JsonProperty("travelerPrice")
    public void setTravelerPrice(List<TravelerPrice> travelerPrice) {
        this.travelerPrice = travelerPrice;
    }

    @JsonProperty("farePrice")
    public List<FarePrice> getFarePrice() {
        return farePrice;
    }

    @JsonProperty("farePrice")
    public void setFarePrice(List<FarePrice> farePrice) {
        this.farePrice = farePrice;
    }

    @JsonProperty("itineraryRef")
    public ItineraryRef getItineraryRef() {
        return itineraryRef;
    }

    @JsonProperty("itineraryRef")
    public void setItineraryRef(ItineraryRef itineraryRef) {
        this.itineraryRef = itineraryRef;
    }

    @JsonProperty("ancillaryRef")
    public List<Object> getAncillaryRef() {
        return ancillaryRef;
    }

    @JsonProperty("ancillaryRef")
    public void setAncillaryRef(List<Object> ancillaryRef) {
        this.ancillaryRef = ancillaryRef;
    }

    @JsonProperty("miscRef")
    public List<Object> getMiscRef() {
        return miscRef;
    }

    @JsonProperty("miscRef")
    public void setMiscRef(List<Object> miscRef) {
        this.miscRef = miscRef;
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
