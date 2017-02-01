
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
    "userSessionId",
    "pos",
    "error",
    "warning",
    "reservation",
    "refs"
})
public class PaymentResponse {

    @JsonProperty("userSessionId")
    private String userSessionId;
    @JsonProperty("pos")
    private String pos;
    @JsonProperty("error")
    private List<Object> error = null;
    @JsonProperty("warning")
    private List<Warning> warning = null;
    @JsonProperty("reservation")
    private Reservation reservation;
    @JsonProperty("refs")
    private Refs refs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("userSessionId")
    public String getUserSessionId() {
        return userSessionId;
    }

    @JsonProperty("userSessionId")
    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }

    @JsonProperty("pos")
    public String getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(String pos) {
        this.pos = pos;
    }

    @JsonProperty("error")
    public List<Object> getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(List<Object> error) {
        this.error = error;
    }

    @JsonProperty("warning")
    public List<Warning> getWarning() {
        return warning;
    }

    @JsonProperty("warning")
    public void setWarning(List<Warning> warning) {
        this.warning = warning;
    }

    @JsonProperty("reservation")
    public Reservation getReservation() {
        return reservation;
    }

    @JsonProperty("reservation")
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @JsonProperty("refs")
    public Refs getRefs() {
        return refs;
    }

    @JsonProperty("refs")
    public void setRefs(Refs refs) {
        this.refs = refs;
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
