package net.pkhapps.vepari.server.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@link AccessTokenServiceImpl}.
 */
public class AccessTokenServiceImplTest {

    @SuppressWarnings("SpellCheckingInspection")
    private static final String TEST_USER_NAME = "joecool";
    private static final long TEST_USER_ID = 123;
    private static final Duration TOKEN_DURATION = Duration.ofDays(10);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private AccessTokenRepository accessTokenRepository;
    private AccessTokenService accessTokenService;
    private User testUser;

    @Before
    public void setUp() {
        var securityProperties = new SecurityProperties();
        securityProperties.getAccessTokens().setDefaultDuration(TOKEN_DURATION);

        accessTokenRepository = Mockito.mock(AccessTokenRepository.class);
        accessTokenService = new AccessTokenServiceImpl(accessTokenRepository,
                SECURE_RANDOM, securityProperties);
        testUser = new User(TEST_USER_NAME);
        testUser.setId(TEST_USER_ID);

        var savedAccessToken = ArgumentCaptor.forClass(AccessToken.class);
        when(accessTokenRepository.save(savedAccessToken.capture())).then(invocationOnMock -> savedAccessToken.getValue());
    }

    @Test
    public void generateToken_usernamePasswordAuthentication_tokenCreated() {
        AccessToken accessToken = accessTokenService.generateToken(testUser);
        assertThat(accessToken.getUser()).isEqualTo(testUser);
        assertThat(accessToken.getExpirationDate()).isEqualTo(accessToken.getIssueDate().plus(TOKEN_DURATION));

        verify(accessTokenRepository).save(accessToken);
    }

    @Test
    public void invalidateToken() {
        var accessToken = new AccessToken(testUser, SECURE_RANDOM, TOKEN_DURATION);
        accessTokenService.invalidateToken(accessToken.getToken());

        verify(accessTokenRepository).deleteByToken(accessToken.getToken());
    }

    @Test
    public void invalidateAllTokensForCurrentUser() {
        accessTokenService.invalidateAllTokens(testUser);

        verify(accessTokenRepository).deleteByUser(testUser);
    }
}
