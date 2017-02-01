
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
    "typeRef",
    "travelerRef",
    "air"
})
public class TravelerComposition {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("typeRef")
    private TypeRef_______ typeRef;
    @JsonProperty("travelerRef")
    private TravelerRef travelerRef;
    @JsonProperty("air")
    private Air__ air;
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

    @JsonProperty("typeRef")
    public TypeRef_______ getTypeRef() {
        return typeRef;
    }

    @JsonProperty("typeRef")
    public void setTypeRef(TypeRef_______ typeRef) {
        this.typeRef = typeRef;
    }

    @JsonProperty("travelerRef")
    public TravelerRef getTravelerRef() {
        return travelerRef;
    }

    @JsonProperty("travelerRef")
    public void setTravelerRef(TravelerRef travelerRef) {
        this.travelerRef = travelerRef;
    }

    @JsonProperty("air")
    public Air__ getAir() {
        return air;
    }

    @JsonProperty("air")
    public void setAir(Air__ air) {
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
