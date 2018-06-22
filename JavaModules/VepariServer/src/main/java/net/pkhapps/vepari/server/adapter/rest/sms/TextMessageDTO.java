package net.pkhapps.vepari.server.adapter.rest.sms;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * DTO for representing incoming text messages in JSON format.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public class TextMessageDTO {

    @JsonProperty
    @NotNull
    public String sender;

    @JsonProperty
    @NotNull
    public Instant timestamp;

    @JsonProperty
    @NotNull
    public String body;
}
