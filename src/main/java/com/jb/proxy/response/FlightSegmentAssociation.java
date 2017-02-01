
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
    "bookingClass",
    "cabinClass",
    "fareFamilyRef",
    "flightSegmentRef"
})
public class FlightSegmentAssociation {

    @JsonProperty("bookingClass")
    private String bookingClass;
    @JsonProperty("cabinClass")
    private String cabinClass;
    @JsonProperty("fareFamilyRef")
    private FareFamilyRef fareFamilyRef;
    @JsonProperty("flightSegmentRef")
    private FlightSegmentRef_ flightSegmentRef;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("bookingClass")
    public String getBookingClass() {
        return bookingClass;
    }

    @JsonProperty("bookingClass")
    public void setBookingClass(String bookingClass) {
        this.bookingClass = bookingClass;
    }

    @JsonProperty("cabinClass")
    public String getCabinClass() {
        return cabinClass;
    }

    @JsonProperty("cabinClass")
    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    @JsonProperty("fareFamilyRef")
    public FareFamilyRef getFareFamilyRef() {
        return fareFamilyRef;
    }

    @JsonProperty("fareFamilyRef")
    public void setFareFamilyRef(FareFamilyRef fareFamilyRef) {
        this.fareFamilyRef = fareFamilyRef;
    }

    @JsonProperty("flightSegmentRef")
    public FlightSegmentRef_ getFlightSegmentRef() {
        return flightSegmentRef;
    }

    @JsonProperty("flightSegmentRef")
    public void setFlightSegmentRef(FlightSegmentRef_ flightSegmentRef) {
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
