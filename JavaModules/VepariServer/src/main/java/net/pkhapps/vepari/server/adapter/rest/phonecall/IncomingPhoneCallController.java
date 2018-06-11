package net.pkhapps.vepari.server.adapter.rest.phonecall;

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
 * REST controller for receiving notifications of phone calls from a GSM gateway. In other words, we are not interested
 * in what was said or heard in the phone call, only that a phone call was received from a particular number at a
 * particular time. The {@link Permissions#RECEIVE_PHONE_CALL} permission is required for this REST controller to
 * accept the incoming call.
 */
@RestController
@Validated
@RequestMapping(path = IncomingPhoneCallController.PATH)
class IncomingPhoneCallController {

    static final String PATH = "/phoneCall/1.0";
    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingPhoneCallController.class);

    @PostMapping
    @Secured(Permissions.RECEIVE_PHONE_CALL)
    public void receivePhoneCall(@NotNull PhoneCall phoneCall) {
        LOGGER.info("Received phone call {}", phoneCall);
        // TODO Implement me
    }
}
