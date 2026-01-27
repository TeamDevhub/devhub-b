package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.common.provider.TokenIssueProvider;

import java.util.HashMap;
import java.util.Map;

public class FakeTokenIssueProvider implements TokenIssueProvider {

    private static final String ACCESS_PREFIX = "access-token-";
    private static final String REFRESH_PREFIX = "refresh-token-";
    private static final String TEMP_PREFIX = "temp-token-";

    private final Map<String, String> refreshTokenMap = new HashMap<>();

    @Override
    public String createAccessToken(String userGuid, String email, UserRole userRole) {
        return ACCESS_PREFIX + userGuid;
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
