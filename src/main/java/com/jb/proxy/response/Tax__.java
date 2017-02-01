
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
    "code",
    "included",
    "value",
    "description"
})
public class Tax__ {

    @JsonProperty("code")
    private String code;
    @JsonProperty("included")
    private Boolean included;
    @JsonProperty("value")
    private Value__ value;
    @JsonProperty("description")
    private List<Description__> description = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("included")
    public Boolean getIncluded() {
        return included;
    }

    @JsonProperty("included")
    public void setIncluded(Boolean included) {
        this.included = included;
    }

    @JsonProperty("value")
    public Value__ getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Value__ value) {
        this.value = value;
    }

    @JsonProperty("description")
    public List<Description__> getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(List<Description__> description) {
        this.description = description;
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
