
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
    "uri",
    "id",
    "travelerCompositionRef",
    "air"
})
public class Assignment {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("travelerCompositionRef")
    private TravelerCompositionRef travelerCompositionRef;
    @JsonProperty("air")
    private Air air;
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

    @JsonProperty("travelerCompositionRef")
    public TravelerCompositionRef getTravelerCompositionRef() {
        return travelerCompositionRef;
    }

    @JsonProperty("travelerCompositionRef")
    public void setTravelerCompositionRef(TravelerCompositionRef travelerCompositionRef) {
        this.travelerCompositionRef = travelerCompositionRef;
    }

    @JsonProperty("air")
    public Air getAir() {
        return air;
    }

    @JsonProperty("air")
    public void setAir(Air air) {
        this.air = air;
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
