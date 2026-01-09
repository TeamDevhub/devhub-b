package teamdevhub.devhub.small.mock.provider;

import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.auth.TokenIssueProvider;

public class FakeTokenIssueProvider implements TokenIssueProvider {

    private static final String PREFIX = "Bearer ";
    private static final String ACCESS_PREFIX = "access-token-";
    private static final String REFRESH_PREFIX = "refresh-token-";

    @Override
    public String createAccessToken(String userGuid, String email, UserRole userRole) {
        return ACCESS_PREFIX + userGuid;
    }

    @Override
    public String createRefreshToken(String userGuid) {
        return REFRESH_PREFIX + userGuid;
    }

    @Override
    public String extractUserGuidFromRefreshToken(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith(REFRESH_PREFIX)) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
        return refreshToken.substring(REFRESH_PREFIX.length());
    }

    @Override
    public String getPrefix() {
        return PREFIX;
    }
}
