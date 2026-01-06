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
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.common.enums.TokenTypeEnum;
import teamdevhub.devhub.adapter.in.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.provider.TokenProvider;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil implements TokenProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TOKEN_TYPE = "token_type";
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
    public String substringHeaderToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) { return token.substring(7);}
        throw AuthRuleException.of(ErrorCodeEnum.TOKEN_INVALID);
    }

    @Override
    public String getTokenFromHeader(HttpServletRequest req) {
        String token = req.getHeader(AUTHORIZATION_HEADER);
        if(token != null) {
            return URLDecoder.decode(token, StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public String createAccessToken(String email, UserRole userRole) {
        Date now = new Date();
        return Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, userRole)
                        .claim(TOKEN_TYPE, TokenTypeEnum.ACCESS.name())
                        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_TIME))
                        .setIssuedAt(now)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    @Override
    public String getPrefix() {
        return BEARER_PREFIX;
    }

    @Override
    public String createRefreshToken(String email) {
        Date now = new Date();
        return Jwts.builder()
                        .setSubject(email)
                        .claim(TOKEN_TYPE, TokenTypeEnum.REFRESH.name())
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(now)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    @Override
    public JwtStatusEnum validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtStatusEnum.VALID;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            return JwtStatusEnum.INVALID;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
            return JwtStatusEnum.EXPIRED;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            return JwtStatusEnum.INVALID;
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            return JwtStatusEnum.INVALID;
        } finally {
            log.info("JWT 토큰 검증 완료");
        }
    }

    @Override
    public Claims getUserInfo(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    @Override
    public String getEmailFromRefreshToken(String refreshToken) {
        Claims claims = getUserInfo(refreshToken);
        TokenTypeEnum tokenTypeEnum = TokenTypeEnum.valueOf(claims.get(TOKEN_TYPE, String.class));
        if (tokenTypeEnum != TokenTypeEnum.REFRESH) {
            throw AuthRuleException.of(ErrorCodeEnum.TOKEN_INVALID);
        }
        return claims.getSubject();
    }
}