package teamdevhub.devhub.fake.pure.usecase;

import teamdevhub.devhub.adapter.in.dto.response.auth.LoginResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;
import teamdevhub.devhub.port.in.auth.AuthSessionUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

public class FakeAuthSessionUseCase implements AuthSessionUseCase {

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
