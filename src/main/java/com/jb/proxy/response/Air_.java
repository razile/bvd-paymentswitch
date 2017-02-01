
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
    "itinerary",
    "itineraryPrice",
    "fareFamily"
})
public class Air_ {

    @JsonProperty("itinerary")
    private Itinerary itinerary;
    @JsonProperty("itineraryPrice")
    private ItineraryPrice itineraryPrice;
    @JsonProperty("fareFamily")
    private List<FareFamily> fareFamily = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("itinerary")
    public Itinerary getItinerary() {
        return itinerary;
    }

    @JsonProperty("itinerary")
    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    @JsonProperty("itineraryPrice")
    public ItineraryPrice getItineraryPrice() {
        return itineraryPrice;
    }

    @JsonProperty("itineraryPrice")
    public void setItineraryPrice(ItineraryPrice itineraryPrice) {
        this.itineraryPrice = itineraryPrice;
    }

    @JsonProperty("fareFamily")
    public List<FareFamily> getFareFamily() {
        return fareFamily;
    }

    @JsonProperty("fareFamily")
    public void setFareFamily(List<FareFamily> fareFamily) {
        this.fareFamily = fareFamily;
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
