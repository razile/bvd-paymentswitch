
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
    "flightSegmentRef",
    "SSR",
    "FOID",
    "loyalty"
})
public class FlightSegment {

    @JsonProperty("flightSegmentRef")
    private FlightSegmentRef flightSegmentRef;
    @JsonProperty("SSR")
    private List<Object> sSR = null;
    @JsonProperty("FOID")
    private List<Object> fOID = null;
    @JsonProperty("loyalty")
    private List<Loyalty_> loyalty = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("flightSegmentRef")
    public FlightSegmentRef getFlightSegmentRef() {
        return flightSegmentRef;
    }

    @JsonProperty("flightSegmentRef")
    public void setFlightSegmentRef(FlightSegmentRef flightSegmentRef) {
        this.flightSegmentRef = flightSegmentRef;
    }

    @JsonProperty("SSR")
    public List<Object> getSSR() {
        return sSR;
    }

    @JsonProperty("SSR")
    public void setSSR(List<Object> sSR) {
        this.sSR = sSR;
    }

    @JsonProperty("FOID")
    public List<Object> getFOID() {
        return fOID;
    }

    @JsonProperty("FOID")
    public void setFOID(List<Object> fOID) {
        this.fOID = fOID;
    }

    @JsonProperty("loyalty")
    public List<Loyalty_> getLoyalty() {
        return loyalty;
    }

    @JsonProperty("loyalty")
    public void setLoyalty(List<Loyalty_> loyalty) {
        this.loyalty = loyalty;
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
