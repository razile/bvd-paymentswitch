
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
    "location",
    "airline",
    "travelerType"
})
public class Refs {

    @JsonProperty("location")
    private List<Location> location = null;
    @JsonProperty("airline")
    private List<Airline> airline = null;
    @JsonProperty("travelerType")
    private List<TravelerType> travelerType = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("location")
    public List<Location> getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(List<Location> location) {
        this.location = location;
    }

    @JsonProperty("airline")
    public List<Airline> getAirline() {
        return airline;
    }

    @JsonProperty("airline")
    public void setAirline(List<Airline> airline) {
        this.airline = airline;
    }

    @JsonProperty("travelerType")
    public List<TravelerType> getTravelerType() {
        return travelerType;
    }

    @JsonProperty("travelerType")
    public void setTravelerType(List<TravelerType> travelerType) {
        this.travelerType = travelerType;
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
