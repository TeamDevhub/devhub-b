package teamdevhub.devhub.outbound.auth.infrastructure.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.HeaderProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.HttpResponse;

@Component
@RequiredArgsConstructor
public class WebClientOauthHttpClient implements OauthHttpClient {

    private final WebClient oauthWebClient;
    private final ObjectMapper objectMapper;

    @Override
    public <T> HttpResponse<T> postFormUrlEncoded(String uri, MultiValueMap<String, String> formData, HeaderProvider headerProvider, Class<T> responseType) {
        HttpHeaders httpHeaders = headerProvider.get();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return oauthWebClient.post()
                .uri(uri)
                .headers(headers -> headers.addAll(httpHeaders))
                .bodyValue(formData)
                .exchangeToMono(response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> buildResponse(
                                        response.statusCode(),
                                        response.headers().asHttpHeaders(),
                                        body,
                                        responseType
                                ))
                )
                .block();
    }

    @Override
    public <T> HttpResponse<T> get(String uri, HeaderProvider headerProvider, Class<T> responseType) {
        HttpHeaders httpHeaders = headerProvider.get();

        return oauthWebClient.get()
                .uri(uri)
                .headers(headers -> httpHeaders.addAll(httpHeaders))
                .exchangeToMono(response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> buildResponse(
                                        response.statusCode(),
                                        response.headers().asHttpHeaders(),
                                        body,
                                        responseType
                                ))
                )
                .block();
    }

    private <T> HttpResponse<T> buildResponse(HttpStatusCode httpStatusCode, HttpHeaders httpHeaders, String rawBody, Class<T> responseType) {
        T body = null;

        try {
            if (!rawBody.isBlank()) {
                body = objectMapper.readValue(rawBody, responseType);
            }
        } catch (Exception ignored) {

        }

        return new HttpResponse<>(httpStatusCode.value(), body, rawBody, httpHeaders);
    }
}