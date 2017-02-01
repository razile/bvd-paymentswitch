
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
    "securityCode",
    "billingAddressRef",
    "issuingCountry",
    "typeRef",
    "expiryDate",
    "number",
    "holderName"
})
public class PaymentCard {

    @JsonProperty("securityCode")
    private String securityCode;
    @JsonProperty("billingAddressRef")
    private BillingAddressRef billingAddressRef;
    @JsonProperty("issuingCountry")
    private String issuingCountry;
    @JsonProperty("typeRef")
    private TypeRef typeRef;
    @JsonProperty("expiryDate")
    private String expiryDate;
    @JsonProperty("number")
    private String number;
    @JsonProperty("holderName")
    private String holderName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("securityCode")
    public String getSecurityCode() {
        return securityCode;
    }

    @JsonProperty("securityCode")
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @JsonProperty("billingAddressRef")
    public BillingAddressRef getBillingAddressRef() {
        return billingAddressRef;
    }

    @JsonProperty("billingAddressRef")
    public void setBillingAddressRef(BillingAddressRef billingAddressRef) {
        this.billingAddressRef = billingAddressRef;
    }

    @JsonProperty("issuingCountry")
    public String getIssuingCountry() {
        return issuingCountry;
    }

    @JsonProperty("issuingCountry")
    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    @JsonProperty("typeRef")
    public TypeRef getTypeRef() {
        return typeRef;
    }

    @JsonProperty("typeRef")
    public void setTypeRef(TypeRef typeRef) {
        this.typeRef = typeRef;
    }

    @JsonProperty("expiryDate")
    public String getExpiryDate() {
        return expiryDate;
    }

    @JsonProperty("expiryDate")
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    @JsonProperty("number")
    public String getNumber() {
        return number;
    }

    @JsonProperty("number")
    public void setNumber(String number) {
        this.number = number;
    }

    @JsonProperty("holderName")
    public String getHolderName() {
        return holderName;
    }

    @JsonProperty("holderName")
    public void setHolderName(String holderName) {
        this.holderName = holderName;
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
