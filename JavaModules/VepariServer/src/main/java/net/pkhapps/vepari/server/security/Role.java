package net.pkhapps.vepari.server.security;

import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entity representing a role, which essentially is a named collection of {@link GrantedAuthority authorities}.
 * The idea is that even though users are assigned roles, the application itself should not need to worry about the
 * user's role but the user's authorities. This makes it possible to control access to the application in a very
 * fine-grained way.
 */
@Entity
@Table(name = "_roles")
public class Role extends SecurityEntity<Role> {

    @Column(nullable = false, unique = true)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "_role_authorities")
    private Set<String> authorities = new HashSet<>();

    /**
     * Used by JPA only.
     */
    @SuppressWarnings("unused")
    private Role() {
    }

    private Role(@NonNull Role original) {
        super(original);
        name = original.name;
        authorities = new HashSet<>(original.authorities);
    }

    /**
     * Creates a new role with the given name and no authorities.
     */
    Role(@NonNull String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        registerEvent(new RoleCreatedEvent(this));
    }

    @Override
    public Role copy() {
        return new Role(this);
    }

    /**
     * Returns the name of the role.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the role.
     */
    @NonNull
    public Role rename(@NonNull String newName) {
        var oldName = name;
        if (!Objects.equals(oldName, newName)) {
            name = Objects.requireNonNull(newName, "newName must not be null");
            registerEvent(new RoleRenamedEvent(this, oldName));
        }
        return this;
    }

    /**
     * Returns all authorities that this role has. The returned collection is a copy and modifying it will
     * not affect the role. Use {@link #addAuthority(GrantedAuthority)} and {@link #removeAuthority(GrantedAuthority)}
     * to modify the collection.
     */
    @NonNull
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    /**
     * Adds the given authority to the role. If the authority has already been added, nothing happens.
     */
    @NonNull
    public Role addAuthority(@NonNull GrantedAuthority authority) {
        Objects.requireNonNull(authority, "authority must not be null");
        if (authorities.add(authority.getAuthority())) {
            registerEvent(new RoleAuthorityAddedEvent(this, authority));
        }
        return this;
    }

    /**
     * Adds the given authority to the role. If the authority has already been added, nothing happens.
     */
    @NonNull
    public Role addAuthority(@NonNull String authority) {
        Objects.requireNonNull(authority, "authority must not be null");
        return addAuthority(new SimpleGrantedAuthority(authority));
    }

    /**
     * Removes the given authority from the role. If the authority did not exist, nothing happens.
     */
    @NonNull
    public Role removeAuthority(@NonNull GrantedAuthority authority) {
        Objects.requireNonNull(authority, "authority must not be null");
        if (authorities.remove(authority.getAuthority())) {
            registerEvent(new RoleAuthorityRemovedEvent(this, authority));
        }
        return this;
    }

    /**
     * Event fired when a {@link Role} is created.
     */
    public static final class RoleCreatedEvent extends SecurityEntityEvent<Role> {

        RoleCreatedEvent(@NonNull Role sender) {
            super(sender);
        }
    }

    /**
     * Event fired when a {@link Role} is renamed.
     */
    public static final class RoleRenamedEvent extends SecurityEntityEvent<Role> {

        private final String oldName;

        RoleRenamedEvent(@NonNull Role sender, @NonNull String oldName) {
            super(sender);
            this.oldName = Objects.requireNonNull(oldName, "oldName must not be null");
        }

        /**
         * Returns the old name of the role.
         */
        @NonNull
        public String getOldName() {
            return oldName;
        }
    }

    /**
     * Base class for {@link Role role events} that concern a particular {@link GrantedAuthority}.
     */
    static abstract class RoleAuthorityChangeEvent extends SecurityEntityEvent<Role> {

        private final GrantedAuthority authority;

        RoleAuthorityChangeEvent(@NonNull Role sender, @NonNull GrantedAuthority authority) {
            super(sender);
            this.authority = Objects.requireNonNull(authority, "authority must not be null");
        }

        /**
         * Returns the authority that this event concerns.
         */
        @NonNull
        public GrantedAuthority getAuthority() {
            return authority;
        }
    }

    /**
     * Event fired when a {@link GrantedAuthority} is added to a {@link Role}.
     */
    public static final class RoleAuthorityAddedEvent extends RoleAuthorityChangeEvent {

        RoleAuthorityAddedEvent(@NonNull Role sender, @NonNull GrantedAuthority authority) {
            super(sender, authority);
        }
    }

    /**
     * Event fired when a {@link GrantedAuthority} is removed from a {@link Role}.
     */
    public static final class RoleAuthorityRemovedEvent extends RoleAuthorityChangeEvent {

        RoleAuthorityRemovedEvent(@NonNull Role sender, @NonNull GrantedAuthority authority) {
            super(sender, authority);
        }
    }
}
