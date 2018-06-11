package net.pkhapps.vepari.server.adapter.rest.sms;

import net.pkhapps.vepari.server.security.Permissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * REST controller for receiving SMS:es from a GSM gateway. The {@link Permissions#RECEIVE_SMS} permission is required
 * for this REST controller to accept the incoming SMS.
 */
@RestController
@Validated
@RequestMapping(path = IncomingSMSController.PATH)
class IncomingSMSController {

    static final String PATH = "/sms/1.0";
    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingSMSController.class);

    @PostMapping
    @Secured(Permissions.RECEIVE_SMS)
    public void receiveSMS(@NotNull SMS sms) {
        LOGGER.info("Received SMS {}", sms);
        // TODO Implement me
    }
}
