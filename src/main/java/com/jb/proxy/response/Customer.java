
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
    "name",
    "email",
    "phone",
    "address",
    "FOID",
    "extraInfo",
    "loyalty"
})
public class Customer {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private Name_ name;
    @JsonProperty("email")
    private List<Email_> email = null;
    @JsonProperty("phone")
    private List<Phone_> phone = null;
    @JsonProperty("address")
    private List<Address_> address = null;
    @JsonProperty("FOID")
    private List<Object> fOID = null;
    @JsonProperty("extraInfo")
    private List<Object> extraInfo = null;
    @JsonProperty("loyalty")
    private List<Loyalty__> loyalty = null;
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

    @JsonProperty("name")
    public Name_ getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(Name_ name) {
        this.name = name;
    }

    @JsonProperty("email")
    public List<Email_> getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(List<Email_> email) {
        this.email = email;
    }

    @JsonProperty("phone")
    public List<Phone_> getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(List<Phone_> phone) {
        this.phone = phone;
    }

    @JsonProperty("address")
    public List<Address_> getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(List<Address_> address) {
        this.address = address;
    }

    @JsonProperty("FOID")
    public List<Object> getFOID() {
        return fOID;
    }

    @JsonProperty("FOID")
    public void setFOID(List<Object> fOID) {
        this.fOID = fOID;
    }

    @JsonProperty("extraInfo")
    public List<Object> getExtraInfo() {
        return extraInfo;
    }

    @JsonProperty("extraInfo")
    public void setExtraInfo(List<Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    @JsonProperty("loyalty")
    public List<Loyalty__> getLoyalty() {
        return loyalty;
    }

    @JsonProperty("loyalty")
    public void setLoyalty(List<Loyalty__> loyalty) {
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
