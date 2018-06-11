package net.pkhapps.vepari.server.adapter.rest.phonecall;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.Objects;

public class PhoneCall {

    @JsonProperty(required = true)
    private String caller;
    @JsonProperty(required = true)
    private Instant timestamp;

    @JsonCreator
    public PhoneCall(@NonNull String caller, @NonNull Instant timestamp) {
        this.caller = Objects.requireNonNull(caller);
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    @NonNull
    public String getCaller() {
        return caller;
    }

    @NonNull
    public Instant getTimestamp() {
        return timestamp;
    }
}
