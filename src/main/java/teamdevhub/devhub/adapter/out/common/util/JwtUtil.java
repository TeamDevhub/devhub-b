package teamdevhub.devhub.adapter.out.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import teamdevhub.devhub.adapter.out.common.exception.AuthRuleException;
import teamdevhub.devhub.common.JwtClaims;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.TokenTypeEnum;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.provider.TokenProvider;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil implements TokenProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;
    private static final long REFRESH_TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    @Override
    public String createAccessToken(String userGuid, String email, UserRole userRole) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userGuid)
                .claim(JwtClaims.EMAIL, email)
                .claim(JwtClaims.USER_ROLE, userRole.name())
                .claim(JwtClaims.TOKEN_TYPE, TokenTypeEnum.ACCESS.name())
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    @Override
    public String createRefreshToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                        .setSubject(email)
                        .claim(JwtClaims.TOKEN_TYPE, TokenTypeEnum.REFRESH.name())
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(now)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    @Override
    public Claims parseClaims(String token) {

        if (!StringUtils.hasText(token)) {
            throw AuthRuleException.of(ErrorCodeEnum.TOKEN_INVALID);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw AuthRuleException.of(ErrorCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw AuthRuleException.of(ErrorCodeEnum.TOKEN_INVALID);
        }
    }

    @Override
    public String extractEmailFromRefreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        TokenTypeEnum tokenTypeEnum = TokenTypeEnum.valueOf(claims.get(JwtClaims.TOKEN_TYPE, String.class));
        if (tokenTypeEnum != TokenTypeEnum.REFRESH) {
            throw AuthRuleException.of(ErrorCodeEnum.TOKEN_INVALID);
        }
        return claims.getSubject();
    }

    @Override
    public String resolveToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(AUTHORIZATION_HEADER);
    }

    @Override
    public String removeBearer(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        throw AuthRuleException.of(ErrorCodeEnum.TOKEN_INVALID);
    }

    @Override
    public String getPrefix() {
        return BEARER_PREFIX;
    }
}
