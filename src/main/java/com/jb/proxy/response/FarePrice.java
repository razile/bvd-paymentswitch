
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
    "departureDate",
    "code",
    "baseFare",
    "totalFare",
    "tax",
    "fee",
    "promotion",
    "departureAirportRef",
    "arrivalAirportRef",
    "filingAirlineRef",
    "flightSegmentAssociation",
    "fareRef"
})
public class FarePrice {

    @JsonProperty("departureDate")
    private String departureDate;
    @JsonProperty("code")
    private String code;
    @JsonProperty("baseFare")
    private BaseFare___ baseFare;
    @JsonProperty("totalFare")
    private TotalFare___ totalFare;
    @JsonProperty("tax")
    private List<Object> tax = null;
    @JsonProperty("fee")
    private List<Object> fee = null;
    @JsonProperty("promotion")
    private List<Object> promotion = null;
    @JsonProperty("departureAirportRef")
    private DepartureAirportRef departureAirportRef;
    @JsonProperty("arrivalAirportRef")
    private ArrivalAirportRef arrivalAirportRef;
    @JsonProperty("filingAirlineRef")
    private FilingAirlineRef filingAirlineRef;
    @JsonProperty("flightSegmentAssociation")
    private List<FlightSegmentAssociation> flightSegmentAssociation = null;
    @JsonProperty("fareRef")
    private FareRef fareRef;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("departureDate")
    public String getDepartureDate() {
        return departureDate;
    }

    @JsonProperty("departureDate")
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("baseFare")
    public BaseFare___ getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BaseFare___ baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("totalFare")
    public TotalFare___ getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(TotalFare___ totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("tax")
    public List<Object> getTax() {
        return tax;
    }

    @JsonProperty("tax")
    public void setTax(List<Object> tax) {
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

    @JsonProperty("departureAirportRef")
    public DepartureAirportRef getDepartureAirportRef() {
        return departureAirportRef;
    }

    @JsonProperty("departureAirportRef")
    public void setDepartureAirportRef(DepartureAirportRef departureAirportRef) {
        this.departureAirportRef = departureAirportRef;
    }

    @JsonProperty("arrivalAirportRef")
    public ArrivalAirportRef getArrivalAirportRef() {
        return arrivalAirportRef;
    }

    @JsonProperty("arrivalAirportRef")
    public void setArrivalAirportRef(ArrivalAirportRef arrivalAirportRef) {
        this.arrivalAirportRef = arrivalAirportRef;
    }

    @JsonProperty("filingAirlineRef")
    public FilingAirlineRef getFilingAirlineRef() {
        return filingAirlineRef;
    }

    @JsonProperty("filingAirlineRef")
    public void setFilingAirlineRef(FilingAirlineRef filingAirlineRef) {
        this.filingAirlineRef = filingAirlineRef;
    }

    @JsonProperty("flightSegmentAssociation")
    public List<FlightSegmentAssociation> getFlightSegmentAssociation() {
        return flightSegmentAssociation;
    }

    @JsonProperty("flightSegmentAssociation")
    public void setFlightSegmentAssociation(List<FlightSegmentAssociation> flightSegmentAssociation) {
        this.flightSegmentAssociation = flightSegmentAssociation;
    }

    @JsonProperty("fareRef")
    public FareRef getFareRef() {
        return fareRef;
    }

    @JsonProperty("fareRef")
    public void setFareRef(FareRef fareRef) {
        this.fareRef = fareRef;
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
