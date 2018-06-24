package net.pkhapps.vepari.server.adapter.rest.security;

import net.pkhapps.vepari.server.security.AccessTokenService;
import net.pkhapps.vepari.server.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * REST controller for authentication. Typically, clients use Basic authentication to create a token and then use
 * the token for all future calls.
 */
@RestController
@RequestMapping(path = AuthenticationController.PATH)
@Validated
class AuthenticationController {

    static final String PATH = "/auth/1.0";

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AccessTokenService accessTokenService;

    AuthenticationController(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @PostMapping(path = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public AccessTokenDTO createToken(@AuthenticationPrincipal @NotNull User principal) {
        logger.debug("Creating new token for {}", principal);
        return new AccessTokenDTO(accessTokenService.generateToken(principal));
    }

    @GetMapping(path = "/authorities", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean hasAuthority(@AuthenticationPrincipal @NotNull User principal,
                                @RequestParam("authority") @NotBlank String authority) {
        logger.debug("Checking if {} has authority {}", principal, authority);
        return principal.hasAuthority(authority);
    }
}
