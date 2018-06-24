package net.pkhapps.vepari.server.adapter.rest.alert;

import net.pkhapps.vepari.server.security.Permissions;
import net.pkhapps.vepari.server.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * TODO implement me
 */
@RestController
@RequestMapping(path = AlertController.PATH)
@Validated
class AlertController {

    static final String PATH = "/alert/1.0";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Secured({Permissions.RECEIVE_ALERT, Permissions.RECEIVE_ALERT_FULL_DETAILS})
    public List<Long> getUnseenAlertRecordIds(@AuthenticationPrincipal @NotNull User principal) {
        return Collections.emptyList();
    }

    @GetMapping(path = "{alertRecordId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Secured({Permissions.RECEIVE_ALERT, Permissions.RECEIVE_ALERT_FULL_DETAILS})
    public ResponseEntity<AlertDTO> getAlert(@AuthenticationPrincipal @NotNull User principal,
                                             @PathVariable("alertRecordId") @NotNull Long alertRecordId) {
        return null;
    }

    @PostMapping(path = "{alertRecordId}/ack")
    @Secured({Permissions.RECEIVE_ALERT, Permissions.RECEIVE_ALERT_FULL_DETAILS})
    public void acknowledgeAlert(@AuthenticationPrincipal @NotNull User principal,
                                 @PathVariable("alertRecordId") @NotNull Long alertRecordId) {
    }
}
