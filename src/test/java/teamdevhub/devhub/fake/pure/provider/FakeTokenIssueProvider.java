package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;

import java.util.HashMap;
import java.util.Map;

public class FakeTokenIssueProvider implements TokenIssueProvider {

    private static final String PREFIX = "Bearer ";
    private static final String ACCESS_PREFIX = "access-token-";
    private static final String REFRESH_PREFIX = "refresh-token-";

    private final Map<String, String> refreshTokenMap = new HashMap<>();

    @Override
    public String createAccessToken(String userGuid, String email, UserRole userRole) {
        return ACCESS_PREFIX + userGuid;
    }

    @Override
    public String createRefreshToken(String userGuid) {
        String token = REFRESH_PREFIX + userGuid;
        refreshTokenMap.put(token, userGuid); // 토큰 저장
        return token;
    }

    @Override
    public String extractUserGuidFromRefreshToken(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith(REFRESH_PREFIX)) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        String userGuid = refreshTokenMap.get(refreshToken);
        if (userGuid == null) {
            return refreshToken.substring(REFRESH_PREFIX.length());
        }

        return userGuid;
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
