package teamdevhub.devhub.outbound.security.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import teamdevhub.devhub.core.auth.domain.UserCredential;

import java.util.Collection;
import java.util.List;

public class UserAuthentication implements UserDetails {

    private final UserCredential userCredential;

    public UserAuthentication(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    public UserCredential getUser() {
        return userCredential;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userCredential.userGuid();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userCredential.userRole().getAuthority()));
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
        return userCredential.userGuid();
    }
}
