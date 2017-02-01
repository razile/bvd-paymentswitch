
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
    "dateOfBirth",
    "gender",
    "name",
    "email",
    "phone",
    "address",
    "FOID",
    "typeRef",
    "loyalty",
    "extraInfo",
    "assignment"
})
public class Traveler {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("name")
    private Name name;
    @JsonProperty("email")
    private List<Email> email = null;
    @JsonProperty("phone")
    private List<Phone> phone = null;
    @JsonProperty("address")
    private List<Address> address = null;
    @JsonProperty("FOID")
    private List<Object> fOID = null;
    @JsonProperty("typeRef")
    private TypeRef___ typeRef;
    @JsonProperty("loyalty")
    private List<Loyalty> loyalty = null;
    @JsonProperty("extraInfo")
    private List<Object> extraInfo = null;
    @JsonProperty("assignment")
    private List<Assignment> assignment = null;
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

    @JsonProperty("dateOfBirth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @JsonProperty("dateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @JsonProperty("gender")
    public String getGender() {
        return gender;
    }

    @JsonProperty("gender")
    public void setGender(String gender) {
        this.gender = gender;
    }

    @JsonProperty("name")
    public Name getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(Name name) {
        this.name = name;
    }

    @JsonProperty("email")
    public List<Email> getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(List<Email> email) {
        this.email = email;
    }

    @JsonProperty("phone")
    public List<Phone> getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(List<Phone> phone) {
        this.phone = phone;
    }

    @JsonProperty("address")
    public List<Address> getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(List<Address> address) {
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

    @JsonProperty("typeRef")
    public TypeRef___ getTypeRef() {
        return typeRef;
    }

    @JsonProperty("typeRef")
    public void setTypeRef(TypeRef___ typeRef) {
        this.typeRef = typeRef;
    }

    @JsonProperty("loyalty")
    public List<Loyalty> getLoyalty() {
        return loyalty;
    }

    @JsonProperty("loyalty")
    public void setLoyalty(List<Loyalty> loyalty) {
        this.loyalty = loyalty;
    }

    @JsonProperty("extraInfo")
    public List<Object> getExtraInfo() {
        return extraInfo;
    }

    @JsonProperty("extraInfo")
    public void setExtraInfo(List<Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    @JsonProperty("assignment")
    public List<Assignment> getAssignment() {
        return assignment;
    }

    @JsonProperty("assignment")
    public void setAssignment(List<Assignment> assignment) {
        this.assignment = assignment;
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
