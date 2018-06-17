package net.pkhapps.vepari.server.integrationtest;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test for {@link net.pkhapps.vepari.server.adapter.rest.security.AuthenticationController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerIntegrationTest {

    @Autowired
    IntegrationTestUtils integrationTestUtils;


}
