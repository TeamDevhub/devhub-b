package teamdevhub.devhub.port.in.common;

import teamdevhub.devhub.common.util.SecurityUtil;
import teamdevhub.devhub.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("userAuthChecker")
@RequiredArgsConstructor
public class UserAuthChecker {

    private final UserPort userPort;

    public boolean isSelf(String userGuid) {
        String currentUserGuid = SecurityUtil.getCurrentUserGuid();
        return userGuid != null && userGuid.equals(currentUserGuid);
    }
}
