package teamdevhub.devhub.fake.pure.application.provider;

import teamdevhub.devhub.core.auth.port.out.token.TokenIssueProvider;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.HashMap;
import java.util.Map;

public class FakeTokenIssueProvider implements TokenIssueProvider {

    private static final String ACCESS_PREFIX = "access-token-";
    private static final String REFRESH_PREFIX = "refresh-token-";
    private static final String TEMP_PREFIX = "temp-token-";

    private final Map<String, String> refreshTokenMap = new HashMap<>();

    @Override
    public String createAccessToken(UserCredential userCredential) {
        return ACCESS_PREFIX + userCredential.userGuid();
    }

    @Override
    public String createRefreshToken(String userGuid) {
        String token = REFRESH_PREFIX + userGuid;
        refreshTokenMap.put(token, userGuid);
        return token;
    }

    @Override
    public String createTempToken(String oauthId, VerificationProvider verificationProvider, String email) {
        return TEMP_PREFIX + oauthId;
    }
}
