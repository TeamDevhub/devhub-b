package teamdevhub.devhub.adapter.out.common.provider.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.auth.TokenProvider;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {

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
                .claim(JwtClaims.TOKEN_TYPE, TokenType.ACCESS.name())
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    @Override
    public String createRefreshToken(String userGuid) {
        Date now = new Date();
        return Jwts.builder()
                        .setSubject(userGuid)
                        .claim(JwtClaims.TOKEN_TYPE, TokenType.REFRESH.name())
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(now)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    @Override
    public Claims parseClaims(String token) {

        if (!StringUtils.hasText(token)) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw AuthRuleException.of(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
    }

    @Override
    public String extractUserGuidFromRefreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        TokenType tokenType = TokenType.valueOf(claims.get(JwtClaims.TOKEN_TYPE, String.class));
        if (tokenType != TokenType.REFRESH) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
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
        throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
    }

    @Override
    public String getPrefix() {
        return BEARER_PREFIX;
    }
}
