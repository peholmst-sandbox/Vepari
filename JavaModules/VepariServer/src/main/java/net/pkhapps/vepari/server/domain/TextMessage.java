package net.pkhapps.vepari.server.domain;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Value object representing an SMS.
 */
public class TextMessage implements Serializable {

    private final String sender;
    private final Instant timestamp;
    private final String body;

    /**
     * Creates a new text message.
     *
     * @param sender    the number or name of the sender of the message.
     * @param timestamp the timestamp when the message arrived at the SMSC.
     * @param body      the body of the message.
     */
    public TextMessage(@NonNull String sender, @NonNull Instant timestamp, @NonNull String body) {
        this.sender = Objects.requireNonNull(sender, "sender must not be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
        this.body = Objects.requireNonNull(body, "message must not be null");
    }

    /**
     * Returns the number or name of the sender of the message.
     */
    @NonNull
    public String getSender() {
        return sender;
    }

    /**
     * Returns the timestamp when the message arrived at the SMSC.
     */
    @NonNull
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * The body of the message.
     */
    @NonNull
    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        var other = (TextMessage) obj;
        return sender.equals(other.sender) && timestamp.equals(other.timestamp) && body.equals(other.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, timestamp, body);
    }

    @Override
    public String toString() {
        return String.format("%s[sender=%s, timestamp=%s]", getClass().getSimpleName(), sender, timestamp);
    }
}
