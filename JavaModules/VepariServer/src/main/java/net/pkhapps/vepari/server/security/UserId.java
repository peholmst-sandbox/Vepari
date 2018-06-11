package net.pkhapps.vepari.server.security;

import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * A value object used to refer to {@link User}s. This is the preferred way of referring to users from within the
 * rest of the application.
 */
public class UserId {

    private final Long id;

    public UserId(@NonNull Long id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    @NonNull
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return obj == this || id.equals(((UserId) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), id);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), id);
    }
}
