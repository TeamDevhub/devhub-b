package teamdevhub.devhub.domain.auth.vo.token;

import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.TokenType;
import teamdevhub.devhub.domain.user.UserRole;

public record AccessTokenInfo(String userGuid, TokenType tokentype, SignupStatus signupStatus, String email, UserRole userRole) {}
