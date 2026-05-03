package teamdevhub.devhub.outbound.auth.infrastructure.oauth.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthHttpClient;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.config.GithubOauthConfig;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.vo.GithubEmailResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.vo.GithubTokenResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.vo.GithubUserResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.BearerAuthHeaderProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.HttpResponse;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.net.URI;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubOauthClientAdapter implements OauthClient {

    private final OauthHttpClient oauthHttpClient;
    private final GithubOauthConfig githubOauthConfig;

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.GITHUB;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        return UriComponentsBuilder
                .fromUri(URI.create(githubOauthConfig.getAuthorizationUri()))
                .queryParam("client_id", githubOauthConfig.getClientId())
                .queryParam("redirect_uri", githubOauthConfig.getRedirectUri())
                .queryParam("scope", "read:user user:email")
                .queryParam("allow_signup", "true")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public OauthUser fetchUser(String code) {
        String accessToken = getAccessToken(code);

        HttpResponse<GithubUserResponse> response = oauthHttpClient.get(githubOauthConfig.getUserInfoUri(), new BearerAuthHeaderProvider(accessToken), GithubUserResponse.class);

        if (!response.is2xx() || response.body() == null) {
            throw new RuntimeException("Github 사용자 조회 실패: " + response.rawBody());
        }

        GithubUserResponse githubUser = response.body();
        String email = fetchEmailSafe(accessToken, githubUser);

        return new OauthUser(String.valueOf(githubUser.id()), VerificationProvider.GITHUB, email);
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("client_id", githubOauthConfig.getClientId());
        form.add("client_secret", githubOauthConfig.getClientSecret());
        form.add("code", code);

        HttpResponse<GithubTokenResponse> githubToken = oauthHttpClient.postFormUrlEncoded(
                githubOauthConfig.getTokenUri(),
                form,
                () -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.set("Accept", "application/json");
                    return httpHeaders;
                    },
                GithubTokenResponse.class);

        if (!githubToken.is2xx() || githubToken.body() == null || githubToken.body().access_token() == null) {
            throw new RuntimeException("Github 토큰 요청 실패: " + githubToken.rawBody());
        }

        return githubToken.body().access_token();
    }

    private String fetchEmailSafe(String token, GithubUserResponse user) {

        if (user.email() != null && !user.email().isBlank()) {
            return user.email();
        }

        try {
            HttpResponse<GithubEmailResponse[]> githubEmailResponseList = oauthHttpClient.get(
                    githubOauthConfig.getEmailUri(),
                    () -> {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.setBearerAuth(token);
                        httpHeaders.set("Accept", "application/vnd.github+json");
                        return httpHeaders;
                        },
                    GithubEmailResponse[].class);

            if (githubEmailResponseList.is2xx() && githubEmailResponseList.body() != null) {
                return Arrays.stream(githubEmailResponseList.body())
                        .filter(email -> email.primary() && email.verified())
                        .map(GithubEmailResponse::email)
                        .findFirst()
                        .orElse(null);
            }

        } catch (Exception e) {
            log.warn("Github email 조회 실패: {}", e.getMessage());
        }

        return "github_" + user.id() + "@local";
    }
}