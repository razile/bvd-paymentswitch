
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
    "flightSegmentRef"
})
public class FlightSegment__ {

    @JsonProperty("flightSegmentRef")
    private FlightSegmentRef__ flightSegmentRef;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("flightSegmentRef")
    public FlightSegmentRef__ getFlightSegmentRef() {
        return flightSegmentRef;
    }

    @JsonProperty("flightSegmentRef")
    public void setFlightSegmentRef(FlightSegmentRef__ flightSegmentRef) {
        this.flightSegmentRef = flightSegmentRef;
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
