package net.pkhapps.vepari.server.security;

import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
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
    @SuppressWarnings("unused") // Used by JPA only
    private Long optLockVersion;

    @Override
    public final Long getId() {
        return id;
    }

    @Override
    public final boolean isNew() {
        return id == null;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public final boolean equals(Object obj) {
        if (obj == null || !ClassUtils.getUserClass(obj).equals(getClass())) {
            return false;
        } else if (obj == this) {
            return true;
        }

        var other = (SecurityEntity<?>) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return id == null ? super.hashCode() : Objects.hash(getClass(), id);
    }

    @Override
    public final String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), id);
    }
}
