package teamdevhub.devhub.domain.auth.vo.token;

import lombok.Builder;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.domain.user.vo.UserRole;

@Builder
public record AccessTokenInfo(String userGuid, TokenType tokentype, String email, UserRole userRole) {}
