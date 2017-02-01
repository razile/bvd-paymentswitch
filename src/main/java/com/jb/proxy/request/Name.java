
package com.jb.proxy.request;

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
    "last",
    "titleRef",
    "first",
    "middle"
})
public class Name {

    @JsonProperty("last")
    private String last;
    @JsonProperty("titleRef")
    private TitleRef titleRef;
    @JsonProperty("first")
    private String first;
    @JsonProperty("middle")
    private String middle;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("last")
    public String getLast() {
        return last;
    }

    @JsonProperty("last")
    public void setLast(String last) {
        this.last = last;
    }

    @JsonProperty("titleRef")
    public TitleRef getTitleRef() {
        return titleRef;
    }

    @JsonProperty("titleRef")
    public void setTitleRef(TitleRef titleRef) {
        this.titleRef = titleRef;
    }

    @JsonProperty("first")
    public String getFirst() {
        return first;
    }

    @JsonProperty("first")
    public void setFirst(String first) {
        this.first = first;
    }

    @JsonProperty("middle")
    public String getMiddle() {
        return middle;
    }

    @JsonProperty("middle")
    public void setMiddle(String middle) {
        this.middle = middle;
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
