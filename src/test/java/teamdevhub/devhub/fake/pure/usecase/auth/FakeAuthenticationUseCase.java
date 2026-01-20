package teamdevhub.devhub.fake.pure.usecase.auth;

import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

public class FakeAuthenticationUseCase implements AuthenticationUseCase {

    private String revokedUserGuid;
    private String lastReissueRefreshToken;

    @Override
    public LoginResponseDto login(LoginCommand loginCommand) {
        return LoginResponseDto.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();
    }

    @Override
    public LoginResponseDto loginWithOauth(AuthenticatedUser authenticatedUser) {
        return null;
    }

    @Override
    public TokenResponseDto reissueAccessToken(String refreshToken) {
        this.lastReissueRefreshToken = refreshToken;
        return TokenResponseDto.issue("new-access-token");
    }

    @Override
    public void revoke(String userGuid) {
        this.revokedUserGuid = userGuid;
    }

    public String getRevokedUserGuid() {
        return revokedUserGuid;
    }

    public String getLastReissueRefreshToken() {
        return lastReissueRefreshToken;
    }
}
