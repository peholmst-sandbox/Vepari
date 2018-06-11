package net.pkhapps.vepari.server.integrationtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration test for {@link net.pkhapps.vepari.server.adapter.rest.phonecall.IncomingPhoneCallController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IncomingPhoneCallControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void receiveSMS() {

    }
}
