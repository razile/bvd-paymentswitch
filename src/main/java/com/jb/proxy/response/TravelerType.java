
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
    "qualified",
    "groupRef",
    "ageRange",
    "agePromt",
    "description",
    "quantity"
})
public class TravelerType {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("qualified")
    private Boolean qualified;
    @JsonProperty("groupRef")
    private GroupRef groupRef;
    @JsonProperty("ageRange")
    private AgeRange ageRange;
    @JsonProperty("agePromt")
    private String agePromt;
    @JsonProperty("description")
    private List<Description____> description = null;
    @JsonProperty("quantity")
    private Quantity quantity;
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

    @JsonProperty("qualified")
    public Boolean getQualified() {
        return qualified;
    }

    @JsonProperty("qualified")
    public void setQualified(Boolean qualified) {
        this.qualified = qualified;
    }

    @JsonProperty("groupRef")
    public GroupRef getGroupRef() {
        return groupRef;
    }

    @JsonProperty("groupRef")
    public void setGroupRef(GroupRef groupRef) {
        this.groupRef = groupRef;
    }

    @JsonProperty("ageRange")
    public AgeRange getAgeRange() {
        return ageRange;
    }

    @JsonProperty("ageRange")
    public void setAgeRange(AgeRange ageRange) {
        this.ageRange = ageRange;
    }

    @JsonProperty("agePromt")
    public String getAgePromt() {
        return agePromt;
    }

    @JsonProperty("agePromt")
    public void setAgePromt(String agePromt) {
        this.agePromt = agePromt;
    }

    @JsonProperty("description")
    public List<Description____> getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(List<Description____> description) {
        this.description = description;
    }

    @JsonProperty("quantity")
    public Quantity getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
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
