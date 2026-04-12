package teamdevhub.devhub.outbound.auth.infrastructure.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.github.GithubEmailResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.github.GithubTokenResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.github.GithubUserResponse;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubOauthClientAdapter implements OauthClient {

    private final WebClient githubWebClient;
    private final IdentifierProvider identifierProvider;

    @Value("${oauth.github.client-id}")
    private String clientId;

    @Value("${oauth.github.client-secret}")
    private String clientSecret;

    @Value("${oauth.github.redirect-uri}")
    private String redirectUri;

    @Override
    public boolean supports(VerificationProvider provider) {
        return provider == VerificationProvider.GITHUB;
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", "read:user user:email")
                .queryParam("state", identifierProvider.generateIdentifier())
                .build()
                .toUriString();
    }

    @Override
    public OauthUser fetchUser(String code) {

        String token = fetchAccessToken(code);
        GithubUserResponse user = fetchGithubUser(token);
        String email = fetchEmailSafe(token, user);

        return new OauthUser(
                String.valueOf(user.id()),
                VerificationProvider.GITHUB,
                email
        );
    }

    private String fetchAccessToken(String code) {

        GithubTokenResponse response = githubWebClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .header("Accept", "application/json")
                .body(BodyInserters.fromFormData("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("code", code)
                        .with("redirect_uri", redirectUri))
                .retrieve()
                .bodyToMono(GithubTokenResponse.class)
                .block();

        if (response == null || response.access_token() == null) {
            throw new RuntimeException("GitHub token 발급 실패");
        }

        return response.access_token();
    }

    private GithubUserResponse fetchGithubUser(String token) {

        return githubWebClient.get()
                .uri("https://api.github.com/user")
                .header("Authorization", "token " + token)
                .retrieve()
                .bodyToMono(GithubUserResponse.class)
                .block();
    }

    private String fetchEmailSafe(String token, GithubUserResponse user) {

        if (user.email() != null && !user.email().isBlank()) {
            return user.email();
        }

        try {
            GithubEmailResponse[] emails = githubWebClient.get()
                    .uri("https://api.github.com/user/emails")
                    .header("Authorization", "token " + token)
                    .retrieve()
                    .bodyToMono(GithubEmailResponse[].class)
                    .block();

            if (emails != null) {
                return Arrays.stream(emails)
                        .filter(e -> e.primary() && e.verified())
                        .map(GithubEmailResponse::email)
                        .findFirst()
                        .orElse(null);
            }

        } catch (Exception e) {
            log.info("Github email 미존재");
        }

        return "github_" + user.id() + "@noemail.local";
    }
}