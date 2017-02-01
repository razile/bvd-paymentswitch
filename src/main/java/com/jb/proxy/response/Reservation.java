
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
    "code",
    "creationDate",
    "status",
    "traveler",
    "travelerRelation",
    "customer",
    "emergencyContact",
    "item",
    "payment",
    "change"
})
public class Reservation {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("creationDate")
    private String creationDate;
    @JsonProperty("status")
    private String status;
    @JsonProperty("traveler")
    private List<Traveler> traveler = null;
    @JsonProperty("travelerRelation")
    private List<Object> travelerRelation = null;
    @JsonProperty("customer")
    private List<Customer> customer = null;
    @JsonProperty("emergencyContact")
    private List<Object> emergencyContact = null;
    @JsonProperty("item")
    private List<Item> item = null;
    @JsonProperty("payment")
    private List<Payment> payment = null;
    @JsonProperty("change")
    private List<Object> change = null;
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

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("creationDate")
    public String getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creationDate")
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("traveler")
    public List<Traveler> getTraveler() {
        return traveler;
    }

    @JsonProperty("traveler")
    public void setTraveler(List<Traveler> traveler) {
        this.traveler = traveler;
    }

    @JsonProperty("travelerRelation")
    public List<Object> getTravelerRelation() {
        return travelerRelation;
    }

    @JsonProperty("travelerRelation")
    public void setTravelerRelation(List<Object> travelerRelation) {
        this.travelerRelation = travelerRelation;
    }

    @JsonProperty("customer")
    public List<Customer> getCustomer() {
        return customer;
    }

    @JsonProperty("customer")
    public void setCustomer(List<Customer> customer) {
        this.customer = customer;
    }

    @JsonProperty("emergencyContact")
    public List<Object> getEmergencyContact() {
        return emergencyContact;
    }

    @JsonProperty("emergencyContact")
    public void setEmergencyContact(List<Object> emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    @JsonProperty("item")
    public List<Item> getItem() {
        return item;
    }

    @JsonProperty("item")
    public void setItem(List<Item> item) {
        this.item = item;
    }

    @JsonProperty("payment")
    public List<Payment> getPayment() {
        return payment;
    }

    @JsonProperty("payment")
    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }

    @JsonProperty("change")
    public List<Object> getChange() {
        return change;
    }

    @JsonProperty("change")
    public void setChange(List<Object> change) {
        this.change = change;
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
