package teamdevhub.devhub.small.mock.usecase;

import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

public class FakeAuthUseCase implements AuthUseCase {

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
    public void issueRefreshToken(String email, String refreshToken) {
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
