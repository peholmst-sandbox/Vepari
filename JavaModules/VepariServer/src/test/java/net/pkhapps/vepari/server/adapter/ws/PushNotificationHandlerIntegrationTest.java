package net.pkhapps.vepari.server.adapter.ws;

import net.pkhapps.vepari.server.Profiles;
import net.pkhapps.vepari.server.adapter.rest.testing.TestingController;
import net.pkhapps.vepari.server.security.IntegrationTestSecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for {@link PushNotificationHandler}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(Profiles.TESTING)
public class PushNotificationHandlerIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private URI uri;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // Injection works fine
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private IntegrationTestSecurityUtils integrationTestUtils;

    @Before
    public void setUp() {
        uri = URI.create("ws://localhost:" + port + PushNotificationHandler.PATH);
    }

    @Test
    public void ping_validAuthenticationHeader_connectionEstablishedAndPongReceived() throws Exception {
        integrationTestUtils.generateAccessTokenForUserWithPermissions();
        var httpHeaders = integrationTestUtils.addAccessTokenToHeader(new HttpHeaders());
        var webSocketClient = createWebSocketClient();
        var webSocketHandler = new TestWebSocketHandler();
        var session = webSocketClient.doHandshake(webSocketHandler, new WebSocketHttpHeaders(httpHeaders), uri).get();
        session.sendMessage(new PingMessage());
        webSocketHandler.expectMessageOfType(PongMessage.class, pongMessage -> {
            // NOP
        });
        session.close();
    }

    @Test(expected = Exception.class)
    public void handshake_missingAuthenticationHeader_exceptionThrown() throws Exception {
        var webSocketClient = createWebSocketClient();
        var webSocketHandler = new TestWebSocketHandler();
        webSocketClient.doHandshake(webSocketHandler, new WebSocketHttpHeaders(new HttpHeaders()), uri).get();
    }

    @Test
    public void pushNotification() throws Exception {
        integrationTestUtils.generateAccessTokenForUserWithPermissions();
        var httpHeaders = integrationTestUtils.addAccessTokenToHeader(new HttpHeaders());
        var webSocketClient = createWebSocketClient();
        var webSocketHandler = new TestWebSocketHandler();
        var session = webSocketClient.doHandshake(webSocketHandler, new WebSocketHttpHeaders(httpHeaders), uri).get();

        webTestClient.post().uri(TestingController.PATH + TestingController.PUSH_NOTIFICATION_PATH)
                .body(Mono.just("Hello World"), String.class)
                .headers(integrationTestUtils::addAccessTokenToHeader)
                .exchange()
                .expectStatus().isOk();

        webSocketHandler.expectMessageOfType(TextMessage.class,
                textMessage -> assertThat(textMessage.getPayload()).isEqualTo("TEST-EVENT=Hello World"));

        session.close();
    }

    @NonNull
    private WebSocketClient createWebSocketClient() {
        return new StandardWebSocketClient();
    }
}
