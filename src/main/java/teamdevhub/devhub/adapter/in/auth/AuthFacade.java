package teamdevhub.devhub.adapter.in.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthCallbackResponseDto;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.oauth.usecase.OauthCallbackUseCase;
import teamdevhub.devhub.port.in.oauth.usecase.OauthLoginUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.port.in.oauth.command.OauthLoginCommand;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;
import teamdevhub.devhub.port.in.oauth.usecase.OauthSignupUseCase;
import teamdevhub.devhub.port.in.verification.VerificationUseCase;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

@Service
@RequiredArgsConstructor
public class AuthFacade {

    private final AuthenticationUseCase authenticationUseCase;
    private final VerificationUseCase verificationUseCase;
    private final OauthCallbackUseCase oauthCallbackUseCase;
    private final OauthLoginUseCase oauthLoginUseCase;
    private final OauthSignupUseCase oauthSignupUseCase;

    public void issueEmailVerification(IssueVerificationCommand issueVerificationCommand) {
        verificationUseCase.issueVerification(issueVerificationCommand);
    }

    public void confirmEmailVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        verificationUseCase.confirmVerification(confirmVerificationCommand);
    }

    public LoginResponseDto login(LoginCommand loginCommand) {
        return authenticationUseCase.login(loginCommand);
    }

    public LoginResponseDto signupWithOauth(OauthSignupCommand oauthSignupCommand) {
        String tempToken = oauthSignupUseCase.signupWithOauth(oauthSignupCommand);
        OauthLoginCommand oauthLoginCommand = new OauthLoginCommand(tempToken);
        return oauthLoginUseCase.loginWithOauth(oauthLoginCommand);
    }

    public LoginResponseDto loginWithOauth(OauthLoginCommand oauthLoginCommand) {
        return oauthLoginUseCase.loginWithOauth(oauthLoginCommand);
    }

    public TokenResponseDto reissueAccessToken(String refreshToken) {
        return authenticationUseCase.reissueAccessToken(refreshToken);
    }

    public void logout(String userGuid) {
        authenticationUseCase.revoke(userGuid);
    }

    public String createOAuthAuthorizationUrl(String provider) {
        return oauthCallbackUseCase.createAuthorizationUrl(provider);
    }

    public OauthCallbackResponseDto handleOAuthCallback(String provider, String code) {
        return oauthCallbackUseCase.handleOAuthCallback(VerificationProvider.from(provider), code);
    }
}
