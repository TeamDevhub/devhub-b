package teamdevhub.devhub.port.in.common;

import teamdevhub.devhub.common.util.SecurityUtil;
import teamdevhub.devhub.port.out.common.CurrentUserProvider;
import teamdevhub.devhub.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("userAuthChecker")
@RequiredArgsConstructor
public class UserAuthChecker {

    private final CurrentUserProvider currentUserProvider;

    public boolean isSelf(String userGuid) {
        String currentUserGuid = currentUserProvider.getCurrentUserGuid();
        return userGuid != null && userGuid.equals(currentUserGuid);
    }
}
