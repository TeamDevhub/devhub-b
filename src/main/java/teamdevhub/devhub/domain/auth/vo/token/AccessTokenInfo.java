package teamdevhub.devhub.domain.auth.vo.token;

import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.domain.user.UserRole;

public record AccessTokenInfo(String userGuid, SignupStatus signupStatus, String email, UserRole userRole) {}
