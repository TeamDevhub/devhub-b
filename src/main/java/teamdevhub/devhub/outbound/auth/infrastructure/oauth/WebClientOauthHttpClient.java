package teamdevhub.devhub.outbound.auth.infrastructure.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class WebClientOauthHttpClient implements OauthHttpClient {

    private final WebClient webClient;

    @Override
    public <T> T postForm(
            String uri,
            MultiValueMap<String, String> formData,
            Consumer<org.springframework.http.HttpHeaders> headers,
            Class<T> responseType
    ) {
        return webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .headers(headers)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    @Override
    public <T> T get(
            String uri,
            Consumer<org.springframework.http.HttpHeaders> headers,
            Class<T> responseType
    ) {
        return webClient.get()
                .uri(uri)
                .headers(headers)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
}
