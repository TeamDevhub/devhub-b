package teamdevhub.devhub.core.auth.domain.vo.token;

import lombok.Builder;
import teamdevhub.devhub.shared.enums.TokenType;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

@Builder
public record AccessTokenInfo(String userGuid, TokenType tokentype, String email, UserRole userRole) {}
