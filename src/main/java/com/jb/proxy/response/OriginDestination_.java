
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
    "originDestinationRef",
    "flightSegment"
})
public class OriginDestination_ {

    @JsonProperty("originDestinationRef")
    private OriginDestinationRef originDestinationRef;
    @JsonProperty("flightSegment")
    private List<FlightSegment__> flightSegment = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("originDestinationRef")
    public OriginDestinationRef getOriginDestinationRef() {
        return originDestinationRef;
    }

    @JsonProperty("originDestinationRef")
    public void setOriginDestinationRef(OriginDestinationRef originDestinationRef) {
        this.originDestinationRef = originDestinationRef;
    }

    @JsonProperty("flightSegment")
    public List<FlightSegment__> getFlightSegment() {
        return flightSegment;
    }

    @JsonProperty("flightSegment")
    public void setFlightSegment(List<FlightSegment__> flightSegment) {
        this.flightSegment = flightSegment;
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
