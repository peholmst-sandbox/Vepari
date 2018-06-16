package net.pkhapps.vepari.server.security;

import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Base class for security entities. We use a separate base class to keep these completely decoupled
 * from the application domain model in case the security model is refactored out into its own system at some
 * point.
 */
@MappedSuperclass
abstract class SecurityEntity<SE extends SecurityEntity<SE>>
        extends AbstractAggregateRoot<SE> implements Persistable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long optLockVersion;

    /**
     * Default constructor.
     */
    protected SecurityEntity() {
    }

    /**
     * Copy constructor.
     */
    protected SecurityEntity(SecurityEntity<SE> original) {
        Objects.requireNonNull(original, "original must not be null");
        id = original.id;
        optLockVersion = original.optLockVersion;
    }

    @Override
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    protected Long getOptLockVersion() {
        return optLockVersion;
    }

    protected void setOptLockVersion(Long optLockVersion) {
        this.optLockVersion = optLockVersion;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !ClassUtils.getUserClass(obj).equals(getClass())) {
            return false;
        } else if (obj == this) {
            return true;
        }

        var other = (SecurityEntity<?>) obj;
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

    /**
     * Returns a copy of this object.
     */
    @NotNull
    public abstract SE copy();
}
