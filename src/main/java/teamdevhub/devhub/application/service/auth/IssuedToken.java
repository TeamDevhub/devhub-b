package teamdevhub.devhub.application.service.auth;

 record IssuedToken(
        String prefix,
        String accessToken,
        String refreshToken
) {}
