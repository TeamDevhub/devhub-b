package teamdevhub.devhub.adapter.out.provider.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.auth.vo.token.AccessTokenInfo;
import teamdevhub.devhub.domain.auth.vo.token.TempTokenInfo;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenIssueProvider, TokenParseProvider {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 30 * 60 * 1000L;
    private static final long REFRESH_TOKEN_TIME = 60 * 60 * 1000L;
    private static final long TEMP_TOKEN_TIME = 30 * 1000L;

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
        Date now = new Date();
        return Jwts.builder()
                .setSubject(userGuid)
                .claim(JwtClaims.TOKEN_TYPE, TokenType.ACCESS.name())
                .claim(JwtClaims.SIGNUP_STATUS, signupStatus.name())
                .claim(JwtClaims.EMAIL, email)
                .claim(JwtClaims.USER_ROLE, userRole.name())
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
    public String createTempToken(String oauthId, SignupStatus signupStatus, VerificationProvider verificationProvider, String email) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(oauthId)
                .claim(JwtClaims.TOKEN_TYPE, TokenType.TEMP.name())
                .claim(JwtClaims.SIGNUP_STATUS, signupStatus.name())
                .claim(JwtClaims.OAUTH_PROVIDER, verificationProvider.name())
                .claim(JwtClaims.EMAIL, email)
                .setExpiration(new Date(now.getTime() + TEMP_TOKEN_TIME))
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

    @Override
    public AccessTokenInfo getAccessTokenInfo(String token) {
        Claims claims = parseClaims(token);
        TokenType tokenType = extractTokenType(claims);

        if (tokenType != TokenType.ACCESS) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        return new AccessTokenInfo(
                claims.getSubject(),
                SignupStatus.valueOf(claims.get(JwtClaims.SIGNUP_STATUS, String.class)),
                claims.get(JwtClaims.EMAIL, String.class),
                UserRole.valueOf(claims.get(JwtClaims.USER_ROLE, String.class))
        );
    }

    @Override
    public TempTokenInfo getTempTokenInfo(String token) {
        Claims claims = parseClaims(token);
        TokenType tokenType = extractTokenType(claims);

        if (tokenType != TokenType.TEMP) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }

        return new TempTokenInfo(claims.getSubject(),
                SignupStatus.valueOf(claims.get(JwtClaims.SIGNUP_STATUS, String.class)),
                VerificationProvider.valueOf(claims.get(JwtClaims.OAUTH_PROVIDER, String.class)),
                claims.get(JwtClaims.EMAIL, String.class)
        );
    }

    public TokenType extractTokenType(Claims claims) {
        String type = claims.get(JwtClaims.TOKEN_TYPE, String.class);
        if (!StringUtils.hasText(type)) {
            throw AuthRuleException.of(ErrorCode.TOKEN_INVALID);
        }
        return TokenType.valueOf(type);
    }
}
