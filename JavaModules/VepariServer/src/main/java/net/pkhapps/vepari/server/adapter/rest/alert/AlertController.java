package net.pkhapps.vepari.server.adapter.rest.alert;

import net.pkhapps.vepari.server.application.AlertService;
import net.pkhapps.vepari.server.domain.AlertRecord;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST interface for the {@link AlertService}.
 */
@RestController
@RequestMapping(path = AlertController.PATH)
@Validated
class AlertController {

    static final String PATH = "/alert/1.0";
    private final AlertService alertService;

    AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Long> getUnseenAlertRecordIds() {
        return alertService.getUnseenAlertRecords().stream().map(AlertRecord::getId).collect(Collectors.toList());
    }

    @GetMapping(path = "{alertRecordId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AlertDTO> getAlert(@PathVariable("alertRecordId") @NotNull Long alertRecordId) {
        return alertService.getAlert(alertRecordId)
                .map(AlertDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "{alertRecordId}/ack")
    public void acknowledgeAlert(@PathVariable("alertRecordId") @NotNull Long alertRecordId) {
        alertService.acknowledgeAlert(alertRecordId);
    }
}
