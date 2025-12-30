package teamdevhub.devhub.common.auth.userdetails;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserPort userPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 " + email + "은 존재하지 않습니다"));
        return new UserDetailsImpl(user);
    }
}
