package net.pkhapps.vepari.server.adapter.rest.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

public class SMS {

    @JsonProperty(required = true)
    private String sender;
    @JsonProperty(required = true)
    private Instant timestamp;
    @JsonProperty(required = true)
    private String message;

    @JsonCreator
    public SMS(@NonNull String sender, @NonNull Instant timestamp, @NonNull String message) {
        this.sender = Objects.requireNonNull(sender);
        this.timestamp = Objects.requireNonNull(timestamp);
        this.message = Objects.requireNonNull(message);
    }

    @NonNull
    public String getSender() {
        return sender;
    }

    @NonNull
    public Instant getTimestamp() {
        return timestamp;
    }

    @NonNull
    public String getMessage() {
        return message;
    }
}
