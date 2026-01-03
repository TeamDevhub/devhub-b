package teamdevhub.devhub.common.auth.userdetails;

import teamdevhub.devhub.domain.common.record.auth.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private final AuthUser authUser;

    public UserDetailsImpl(AuthUser authUser) {
        this.authUser = authUser;
    }

    public AuthUser getUser() {
        return authUser;
    }

    @Override
    public String getPassword() {
        return authUser.password();
    }

    @Override
    public String getUsername() {
        return authUser.email();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authUser.userRole().getAuthority()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getUserGuid() {
        return authUser.userGuid();
    }
}
