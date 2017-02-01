
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
    "flightNumber",
    "stopQuantity",
    "duration",
    "departure",
    "arrival",
    "operatingAirlineRef",
    "marketingAirlineRef",
    "aircraft",
    "bookingClassAvailability",
    "amenityRef"
})
public class FlightSegment_ {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("flightNumber")
    private String flightNumber;
    @JsonProperty("stopQuantity")
    private Integer stopQuantity;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("departure")
    private Departure departure;
    @JsonProperty("arrival")
    private Arrival arrival;
    @JsonProperty("operatingAirlineRef")
    private OperatingAirlineRef operatingAirlineRef;
    @JsonProperty("marketingAirlineRef")
    private MarketingAirlineRef marketingAirlineRef;
    @JsonProperty("aircraft")
    private Aircraft aircraft;
    @JsonProperty("bookingClassAvailability")
    private List<BookingClassAvailability> bookingClassAvailability = null;
    @JsonProperty("amenityRef")
    private List<Object> amenityRef = null;
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

    @JsonProperty("flightNumber")
    public String getFlightNumber() {
        return flightNumber;
    }

    @JsonProperty("flightNumber")
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    @JsonProperty("stopQuantity")
    public Integer getStopQuantity() {
        return stopQuantity;
    }

    @JsonProperty("stopQuantity")
    public void setStopQuantity(Integer stopQuantity) {
        this.stopQuantity = stopQuantity;
    }

    @JsonProperty("duration")
    public String getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @JsonProperty("departure")
    public Departure getDeparture() {
        return departure;
    }

    @JsonProperty("departure")
    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    @JsonProperty("arrival")
    public Arrival getArrival() {
        return arrival;
    }

    @JsonProperty("arrival")
    public void setArrival(Arrival arrival) {
        this.arrival = arrival;
    }

    @JsonProperty("operatingAirlineRef")
    public OperatingAirlineRef getOperatingAirlineRef() {
        return operatingAirlineRef;
    }

    @JsonProperty("operatingAirlineRef")
    public void setOperatingAirlineRef(OperatingAirlineRef operatingAirlineRef) {
        this.operatingAirlineRef = operatingAirlineRef;
    }

    @JsonProperty("marketingAirlineRef")
    public MarketingAirlineRef getMarketingAirlineRef() {
        return marketingAirlineRef;
    }

    @JsonProperty("marketingAirlineRef")
    public void setMarketingAirlineRef(MarketingAirlineRef marketingAirlineRef) {
        this.marketingAirlineRef = marketingAirlineRef;
    }

    @JsonProperty("aircraft")
    public Aircraft getAircraft() {
        return aircraft;
    }

    @JsonProperty("aircraft")
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    @JsonProperty("bookingClassAvailability")
    public List<BookingClassAvailability> getBookingClassAvailability() {
        return bookingClassAvailability;
    }

    @JsonProperty("bookingClassAvailability")
    public void setBookingClassAvailability(List<BookingClassAvailability> bookingClassAvailability) {
        this.bookingClassAvailability = bookingClassAvailability;
    }

    @JsonProperty("amenityRef")
    public List<Object> getAmenityRef() {
        return amenityRef;
    }

    @JsonProperty("amenityRef")
    public void setAmenityRef(List<Object> amenityRef) {
        this.amenityRef = amenityRef;
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
