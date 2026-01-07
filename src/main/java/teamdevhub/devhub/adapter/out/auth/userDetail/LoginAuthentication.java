package teamdevhub.devhub.adapter.out.auth.userDetail;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import teamdevhub.devhub.domain.common.record.auth.LoginUser;

import java.util.Collection;
import java.util.List;

public class LoginAuthentication implements UserDetails {

    private final LoginUser loginUser;

    public LoginAuthentication(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public LoginUser getUser() {
        return loginUser;
    }

    @Override
    public String getPassword() {
        return loginUser.password();
    }

    @Override
    public String getUsername() {
        return loginUser.email();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(loginUser.userRole().getAuthority()));
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
        return loginUser.userGuid();
    }
}
