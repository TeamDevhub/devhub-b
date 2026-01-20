package teamdevhub.devhub.application.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.oauth.usecase.OauthLoginUseCase;
import teamdevhub.devhub.port.in.oauth.command.OauthLoginCommand;
import teamdevhub.devhub.port.in.user.usecase.UserLoginUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthLoginService implements OauthLoginUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final UserLoginUseCase userLoginUseCase;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponseDto loginWithOauth(OauthLoginCommand oauthLoginCommand) {
        TempTokenInfo tempInfo = tokenIssueProvider.getTempTokenInfo(oauthLoginCommand.tempToken());

        VerificationProvider verificationProvider = tempInfo.verificationProvider();
        String oauthId = tempInfo.oauthId();
        Optional<User> optionalUser = userRepository.findByOAuth(verificationProvider, oauthId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String prefix = tokenIssueProvider.getPrefix();
            String accessToken = tokenIssueProvider.createAccessToken(user.getUserGuid(), user.getEmail(), user.getUserRole());
            String refreshToken = tokenIssueProvider.createRefreshToken(user.getUserGuid());

            userLoginUseCase.updateLastLoginDateTime(user.getUserGuid());
            issueRefreshToken(user.getUserGuid(), refreshToken);
            return LoginResponseDto.existedOAuthUser(prefix, accessToken, refreshToken, user.getSignupStatus());
        } else {
            return LoginResponseDto.notExistedOAuthUser(oauthLoginCommand.tempToken(), SignupStatus.PENDING);
        }
    }

    private void issueRefreshToken(String userGuid, String token) {
        RefreshTokenInfo refreshTokenInfo = RefreshTokenInfo.of(userGuid, token);
        refreshTokenRepository.save(refreshTokenInfo);
    }
}
