package teamdevhub.devhub.port.in.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.out.common.CurrentUserProvider;

@Component("userAuthChecker")
@RequiredArgsConstructor
public class UserAuthChecker {

    private final CurrentUserProvider currentUserProvider;

    public boolean isSelf(String userGuid) {
        String currentUserGuid = currentUserProvider.getCurrentUserGuid();
        return userGuid != null && userGuid.equals(currentUserGuid);
    }
}
