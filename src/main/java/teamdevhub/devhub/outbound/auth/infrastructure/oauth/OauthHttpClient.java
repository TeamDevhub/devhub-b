package teamdevhub.devhub.outbound.auth.infrastructure.oauth;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.function.Consumer;

public interface OauthHttpClient {

    <T> T postForm(
            String uri,
            MultiValueMap<String, String> formData,
            Consumer<HttpHeaders> headers,
            Class<T> responseType
    );

    <T> T get(
            String uri,
            Consumer<HttpHeaders> headers,
            Class<T> responseType
    );
}
