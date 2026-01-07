package teamdevhub.devhub.adapter.out.auth.userDetail;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;

import java.util.Collection;
import java.util.List;

public class LoginAuthentication implements UserDetails {

    private final AuthenticatedUser authenticatedUser;

    public LoginAuthentication(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public AuthenticatedUser getUser() {
        return authenticatedUser;
    }

    @Override
    public String getPassword() {
        return authenticatedUser.password();
    }

    @Override
    public String getUsername() {
        return authenticatedUser.email();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authenticatedUser.userRole().getAuthority()));
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
        return authenticatedUser.userGuid();
    }
}
