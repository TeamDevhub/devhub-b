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
@Table(name = " refresh_token")
public class RefreshTokenEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userGuid;

    @Column(nullable = false, length = 500)
    private String token;

    protected RefreshTokenEntity(String userGuid, String token) {
        this.userGuid = userGuid;
        this.token = token;
    }

    public static RefreshTokenEntity of(String userGuid, String refreshToken) {
        return new RefreshTokenEntity(userGuid, refreshToken);
    }

    public void rotate(String newToken) {
        this.token = newToken;
    }
}
