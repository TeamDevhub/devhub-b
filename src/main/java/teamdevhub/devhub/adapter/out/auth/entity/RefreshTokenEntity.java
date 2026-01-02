package teamdevhub.devhub.adapter.out.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshTokenEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 500)
    private String token;

    protected RefreshTokenEntity(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public static RefreshTokenEntity of(String email, String refreshToken) {
        return new RefreshTokenEntity(email, refreshToken);
    }

    public void rotate(String newToken) {
        this.token = newToken;
    }
}
