package teamdevhub.devhub.application.service.auth;

import lombok.Builder;

@Builder
record IssuedToken(String accessToken, String refreshToken) {}
