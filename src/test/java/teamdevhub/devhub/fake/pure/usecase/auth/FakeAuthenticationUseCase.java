package teamdevhub.devhub.fake.pure.usecase.auth;

import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;

public class FakeAuthenticationUseCase implements AuthenticationUseCase {

    private String revokedUserGuid;
    private AuthenticatedUser lastLoginUser;

    @Override
    public LoginResponseDto login(AuthenticatedUser authenticatedUser) {
        this.lastLoginUser = authenticatedUser;
        return LoginResponseDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();
    }

    @Override
    public TokenResponseDto reissueAccessToken(AuthenticatedUser authenticatedUser) {
        return TokenResponseDto.issueAccessToken("new-access-token");
    }

    @Override
    public void revoke(String userGuid) {
        this.revokedUserGuid = userGuid;
    }

    public String getRevokedUserGuid() {
        return revokedUserGuid;
    }

    public AuthenticatedUser getLastLoginUser() {
        return lastLoginUser;
    }
}
