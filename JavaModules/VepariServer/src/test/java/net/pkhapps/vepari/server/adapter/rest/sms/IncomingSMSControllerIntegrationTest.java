package net.pkhapps.vepari.server.adapter.rest.sms;

import net.pkhapps.vepari.server.security.IntegrationTestSecurityUtils;
import net.pkhapps.vepari.server.security.Permissions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Integration test for {@link IncomingSMSController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IncomingSMSControllerIntegrationTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // Injection works fine
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private IntegrationTestSecurityUtils integrationTestUtils;

    @Test
    public void receiveSMS_authenticationHeaderWithCorrectPermission_accessGranted() {
        integrationTestUtils.generateAccessTokenForUserWithPermissions(Permissions.RECEIVE_SMS);
        webTestClient.post().uri(IncomingSMSController.PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(createDTO()), TextMessageDTO.class)
                .headers(integrationTestUtils::addAccessTokenToHeader)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void receiveSMS_noAuthenticationHeader_accessDenied() {
        webTestClient.post().uri(IncomingSMSController.PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(createDTO()), TextMessageDTO.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void receiveSMS_authenticationHeaderWithIncorrectPermission_accessDenied() {
        integrationTestUtils.generateAccessTokenForUserWithPermissions(Permissions.PREFIX + "NON-EXISTENT");
        webTestClient.post().uri(IncomingSMSController.PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(createDTO()), TextMessageDTO.class)
                .headers(integrationTestUtils::addAccessTokenToHeader)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN);
    }

    private TextMessageDTO createDTO() {
        var dto = new TextMessageDTO();
        dto.sender = "12345";
        dto.timestamp = Instant.now();
        dto.body = "Hello World";
        return dto;
    }
}
