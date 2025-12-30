package teamdevhub.devhub.adapter.out.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshTokenEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 500)
    private String refreshToken;

    protected RefreshTokenEntity(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public static RefreshTokenEntity of(String email, String refreshToken) {
        return new RefreshTokenEntity(email, refreshToken);
    }

    public void rotate(String newToken) {
        this.refreshToken = newToken;
    }
}
