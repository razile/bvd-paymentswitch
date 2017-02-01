
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
    "domain",
    "status",
    "price",
    "air",
    "travelerComposition",
    "assessedPenalty",
    "policy",
    "bookingReference",
    "document"
})
public class Item {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("status")
    private String status;
    @JsonProperty("price")
    private Price price;
    @JsonProperty("air")
    private Air_ air;
    @JsonProperty("travelerComposition")
    private List<TravelerComposition> travelerComposition = null;
    @JsonProperty("assessedPenalty")
    private List<Object> assessedPenalty = null;
    @JsonProperty("policy")
    private List<Policy> policy = null;
    @JsonProperty("bookingReference")
    private List<BookingReference> bookingReference = null;
    @JsonProperty("document")
    private List<Document> document = null;
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

    @JsonProperty("domain")
    public String getDomain() {
        return domain;
    }

    @JsonProperty("domain")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("price")
    public Price getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Price price) {
        this.price = price;
    }

    @JsonProperty("air")
    public Air_ getAir() {
        return air;
    }

    @JsonProperty("air")
    public void setAir(Air_ air) {
        this.air = air;
    }

    @JsonProperty("travelerComposition")
    public List<TravelerComposition> getTravelerComposition() {
        return travelerComposition;
    }

    @JsonProperty("travelerComposition")
    public void setTravelerComposition(List<TravelerComposition> travelerComposition) {
        this.travelerComposition = travelerComposition;
    }

    @JsonProperty("assessedPenalty")
    public List<Object> getAssessedPenalty() {
        return assessedPenalty;
    }

    @JsonProperty("assessedPenalty")
    public void setAssessedPenalty(List<Object> assessedPenalty) {
        this.assessedPenalty = assessedPenalty;
    }

    @JsonProperty("policy")
    public List<Policy> getPolicy() {
        return policy;
    }

    @JsonProperty("policy")
    public void setPolicy(List<Policy> policy) {
        this.policy = policy;
    }

    @JsonProperty("bookingReference")
    public List<BookingReference> getBookingReference() {
        return bookingReference;
    }

    @JsonProperty("bookingReference")
    public void setBookingReference(List<BookingReference> bookingReference) {
        this.bookingReference = bookingReference;
    }

    @JsonProperty("document")
    public List<Document> getDocument() {
        return document;
    }

    @JsonProperty("document")
    public void setDocument(List<Document> document) {
        this.document = document;
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
