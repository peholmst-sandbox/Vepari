package net.pkhapps.vepari.server.domain.base;

import net.pkhapps.vepari.server.security.User;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for aggregate roots.
 */
@MappedSuperclass
@SuppressWarnings({"UnusedReturnValue", "unused"}) // Method-chaining API
public abstract class AggregateRoot<A extends AggregateRoot<A>> extends AbstractAggregateRoot<A>
        implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long optLockVersion;

    @ManyToOne
    @JoinColumn(name = "_created_by")
    @CreatedBy
    private User createdBy;

    @Column(name = "_created_date")
    @CreatedDate
    private Instant createdDate;

    @ManyToOne
    @JoinColumn(name = "_last_modified_by")
    @LastModifiedBy
    private User lastModifiedBy;

    @Column(name = "_last_modified_date")
    @LastModifiedDate
    private Instant lastModifiedDate;

    /**
     * Returns the user who created this aggregate root.
     */
    @NonNull
    public Optional<User> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    /**
     * Sets the user who created this aggregate root. This is normally handled by the repository.
     */
    @NonNull
    protected A setCreatedBy(@Nullable User createdBy) {
        this.createdBy = createdBy;
        return self();
    }

    /**
     * Returns the date when this aggregate root was created.
     */
    @NonNull
    public Optional<Instant> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    /**
     * Sets the date when this aggregate root was created. This is normally handled by the repository.
     */
    @NonNull
    protected A setCreatedDate(@Nullable Instant createdDate) {
        this.createdDate = createdDate;
        return self();
    }

    /**
     * Returns the user who last modified this aggregate root.
     */
    @NonNull
    public Optional<User> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    /**
     * Sets the user who last modified this aggregate root. This is normally handled by the repository.
     */
    @NonNull
    protected A setLastModifiedBy(@Nullable User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return self();
    }

    /**
     * Returns the date when this aggregate root was last modified.
     */
    @NonNull
    public Optional<Instant> getLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    /**
     * Sets the date when this aggregate root was last modified. This is normally handled by the repository.
     */
    @NonNull
    protected A setLastModifiedDate(@Nullable Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return self();
    }

    @Override
    @Nullable
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of this aggregate root. This is normally handled by the repository.
     */
    @NonNull
    protected A setId(@Nullable Long id) {
        this.id = id;
        return self();
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    /**
     * Returns the optimistic locking version of this aggregate root.
     */
    @Nullable
    protected Long getOptLockVersion() {
        return optLockVersion;
    }

    /**
     * Sets the optimistic locking version of this aggregate root. This is normally handled by the repository.
     */
    @NonNull
    protected A setOptLockVersion(@Nullable Long optLockVersion) {
        this.optLockVersion = optLockVersion;
        return self();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private A self() {
        return (A) this;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !ClassUtils.getUserClass(obj).equals(getClass())) {
            return false;
        } else if (obj == this) {
            return true;
        }

        var other = (AggregateRoot<?>) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id == null ? super.hashCode() : Objects.hash(getClass(), id);
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), id);
    }
}
