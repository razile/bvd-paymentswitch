
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
    "name",
    "combination",
    "benefit"
})
public class FareFamily {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private List<Name__> name = null;
    @JsonProperty("combination")
    private List<Object> combination = null;
    @JsonProperty("benefit")
    private List<Benefit> benefit = null;
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

    @JsonProperty("name")
    public List<Name__> getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(List<Name__> name) {
        this.name = name;
    }

    @JsonProperty("combination")
    public List<Object> getCombination() {
        return combination;
    }

    @JsonProperty("combination")
    public void setCombination(List<Object> combination) {
        this.combination = combination;
    }

    @JsonProperty("benefit")
    public List<Benefit> getBenefit() {
        return benefit;
    }

    @JsonProperty("benefit")
    public void setBenefit(List<Benefit> benefit) {
        this.benefit = benefit;
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
