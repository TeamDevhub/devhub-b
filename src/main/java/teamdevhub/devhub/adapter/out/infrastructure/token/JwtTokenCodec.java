package teamdevhub.devhub.adapter.out.infrastructure.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.auth.vo.token.AccessTokenInfo;
import teamdevhub.devhub.domain.auth.vo.token.RefreshTokenInfo;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.provider.TimeProvider;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenCodec implements TokenIssueProvider, TokenParseProvider {

    private final TimeProvider timeProvider;

    private static final String BEARER_PREFIX = "Bearer ";

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
    public String createAccessToken(String userGuid, SignupStatus signupStatus, String email, UserRole userRole) {
        LocalDateTime now = timeProvider.now();
        LocalDateTime expireAt = now.plusMinutes(30);
        return Jwts.builder()
                .setSubject(userGuid)
                .claim(JwtClaims.TOKEN_TYPE, TokenType.ACCESS.name())
                .claim(JwtClaims.SIGNUP_STATUS, signupStatus.name())
                .claim(JwtClaims.EMAIL, email)
                .claim(JwtClaims.USER_ROLE, userRole.name())
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expireAt))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    @Override
    public String createRefreshToken(String userGuid) {
        LocalDateTime now = timeProvider.now();
        LocalDateTime expireAt = now.plusDays(7);
        return Jwts.builder()
                        .setSubject(userGuid)
                        .claim(JwtClaims.TOKEN_TYPE, TokenType.REFRESH.name())
                        .setIssuedAt(toDate(now))
                        .setExpiration(toDate(expireAt))
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    @Override
    public String createTempToken(String oauthId, SignupStatus signupStatus, VerificationProvider verificationProvider, String email) {
        LocalDateTime now = timeProvider.now();
        LocalDateTime expireAt = now.plusMinutes(3);
        return Jwts.builder()
                .setSubject(oauthId)
                .claim(JwtClaims.TOKEN_TYPE, TokenType.TEMP.name())
                .claim(JwtClaims.SIGNUP_STATUS, signupStatus.name())
                .claim(JwtClaims.OAUTH_PROVIDER, verificationProvider.name())
                .claim(JwtClaims.EMAIL, email)
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(expireAt))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    @Override
    public String removeBearer(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
    }

    @Override
    public AccessTokenInfo getAccessTokenInfo(String accessToken) {
        Claims claims = parseClaims(accessToken);
        TokenType tokenType = extractTokenType(claims);

        if (tokenType != TokenType.ACCESS) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        return new AccessTokenInfo(
                claims.getSubject(),
                TokenType.valueOf(claims.get(JwtClaims.TOKEN_TYPE, String.class)),
                SignupStatus.valueOf(claims.get(JwtClaims.SIGNUP_STATUS, String.class)),
                claims.get(JwtClaims.EMAIL, String.class),
                UserRole.valueOf(claims.get(JwtClaims.USER_ROLE, String.class))
        );
    }

    @Override
    public TempTokenInfo getTempTokenInfo(String tempToken) {
        Claims claims = parseClaims(tempToken);
        TokenType tokenType = extractTokenType(claims);

        if (tokenType != TokenType.TEMP) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        return new TempTokenInfo(
                claims.getSubject(),
                TokenType.valueOf(claims.get(JwtClaims.TOKEN_TYPE, String.class)),
                SignupStatus.valueOf(claims.get(JwtClaims.SIGNUP_STATUS, String.class)),
                VerificationProvider.valueOf(claims.get(JwtClaims.OAUTH_PROVIDER, String.class)),
                claims.get(JwtClaims.EMAIL, String.class)
        );
    }

    @Override
    public RefreshTokenInfo getRefreshTokenInfo(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        TokenType tokenType = TokenType.valueOf(claims.get(JwtClaims.TOKEN_TYPE, String.class));
        if (tokenType != TokenType.REFRESH) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
        return new RefreshTokenInfo(claims.getSubject());
    }

    private TokenType extractTokenType(Claims claims) {
        String type = claims.get(JwtClaims.TOKEN_TYPE, String.class);
        if (!StringUtils.hasText(type)) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
        return TokenType.valueOf(type);
    }

    private Claims parseClaims(String token) {

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

    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
