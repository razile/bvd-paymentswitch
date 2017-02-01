
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
    "itemRef",
    "moneyValue"
})
public class ItemAllocation {

    @JsonProperty("itemRef")
    private ItemRef itemRef;
    @JsonProperty("moneyValue")
    private MoneyValue moneyValue;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("itemRef")
    public ItemRef getItemRef() {
        return itemRef;
    }

    @JsonProperty("itemRef")
    public void setItemRef(ItemRef itemRef) {
        this.itemRef = itemRef;
    }

    @JsonProperty("moneyValue")
    public MoneyValue getMoneyValue() {
        return moneyValue;
    }

    @JsonProperty("moneyValue")
    public void setMoneyValue(MoneyValue moneyValue) {
        this.moneyValue = moneyValue;
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
