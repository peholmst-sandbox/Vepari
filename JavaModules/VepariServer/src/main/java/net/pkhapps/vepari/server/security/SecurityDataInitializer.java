package net.pkhapps.vepari.server.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

/**
 * TODO Document me!
 */
@Component
class SecurityDataInitializer {

    private static final String ROLE_GSM_GATEWAY = "GSM_GATEWAY";
    private static final String ROLE_ADMINISTRATOR = "ADMINISTRATOR";
    private static final String USER_ADMINISTRATOR = "admin";

    private final static Logger LOGGER = LoggerFactory.getLogger(SecurityDataInitializer.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    SecurityDataInitializer(RoleRepository roleRepository,
                            UserRepository userRepository,
                            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createInitialData() throws Exception {
        createRoles();
        createAdminUser();
    }

    private void createRoles() {
        saveRoleIfNotExists(new Role(ROLE_GSM_GATEWAY)
                .addAuthority(Permissions.RECEIVE_PHONE_CALL)
                .addAuthority(Permissions.RECEIVE_SMS)
        );
        saveRoleIfNotExists(new Role(ROLE_ADMINISTRATOR)
                .addAuthority(Permissions.INVALIDATE_ACCESS_TOKEN)
        );
    }

    private void saveRoleIfNotExists(@NonNull Role role) {
        if (!roleRepository.existsByName(role.getName())) {
            LOGGER.info("Saving role {}", role.getName());
            roleRepository.saveAndFlush(role);
        } else {
            LOGGER.info("Role {} already exists", role.getName());
        }
    }

    private void createAdminUser() throws Exception {
        if (!userRepository.existsByUsername(USER_ADMINISTRATOR)) {
            LOGGER.info("Saving user {}", USER_ADMINISTRATOR);
            var adminRole = roleRepository.findByName(ROLE_ADMINISTRATOR).orElseThrow(EntityNotFoundException::new);
            var user = new User(USER_ADMINISTRATOR).changePassword("admin123", passwordEncoder).addRole(adminRole);
            userRepository.saveAndFlush(user);
        } else {
            LOGGER.info("User {} already exists", USER_ADMINISTRATOR);
        }
    }
}
