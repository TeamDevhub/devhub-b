package teamdevhub.devhub.outbound.auth.infrastructure.oauth.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthHttpClient;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.config.GithubOauthConfig;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.vo.*;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.net.URI;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubOauthClientAdapter implements OauthClient {

    private final OauthHttpClient oauthHttpClient;
    private final GithubOauthConfig githubOauthConfig;
    private final IdentifierProvider identifierProvider;

    @Override
    public boolean supports(VerificationProvider provider) {
        return provider == VerificationProvider.GITHUB;
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUri(URI.create(githubOauthConfig.getAuthorizationUri()))
                .queryParam("client_id", githubOauthConfig.getClientId())
                .queryParam("redirect_uri", githubOauthConfig.getRedirectUri())
                .queryParam("scope", "read:user user:email")
                .queryParam("allow_signup", "true")
                .queryParam("state", identifierProvider.generateIdentifier())
                .build()
                .toUriString();
    }

    @Override
    public OauthUser fetchUser(String code) {

        String token = getAccessToken(code);

        GithubUserResponse user = oauthHttpClient.get(
                githubOauthConfig.getUserInfoUri(),
                h -> h.setBearerAuth(token),
                GithubUserResponse.class
        );

        String email = fetchEmailSafe(token, user);

        return new OauthUser(
                String.valueOf(user.id()),
                VerificationProvider.GITHUB,
                email
        );
    }

    private String getAccessToken(String code) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", githubOauthConfig.getClientId());
        form.add("client_secret", githubOauthConfig.getClientSecret());
        form.add("code", code);

        GithubTokenResponse res = oauthHttpClient.postForm(
                githubOauthConfig.getTokenUri(),
                form,
                h -> h.set("Accept", "application/json"),
                GithubTokenResponse.class
        );

        return res.access_token();
    }

    private String fetchEmailSafe(String token, GithubUserResponse user) {

        if (user.email() != null && !user.email().isBlank()) {
            return user.email();
        }

        try {
            GithubEmailResponse[] emails = oauthHttpClient.get(
                    githubOauthConfig.getEmailUri(),
                    h -> {
                        h.setBearerAuth(token);
                        h.set("Accept", "application/vnd.github+json");
                    },
                    GithubEmailResponse[].class
            );

            if (emails != null) {
                return Arrays.stream(emails)
                        .filter(e -> e.primary() && e.verified())
                        .map(GithubEmailResponse::email)
                        .findFirst()
                        .orElse(null);
            }

        } catch (Exception e) {
            log.warn("Github email 조회 실패: {}", e.getMessage());
        }

        return "github_" + user.id() + "@noemail.local";
    }
}