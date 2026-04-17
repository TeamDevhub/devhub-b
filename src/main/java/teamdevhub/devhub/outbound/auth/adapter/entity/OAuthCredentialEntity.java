package teamdevhub.devhub.outbound.auth.adapter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "user_oauth_credentials",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_oauth_provider_id",
                        columnNames = {"provider", "oauthId"}
                )
        }
)
public class OAuthCredentialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userGuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationProvider provider;

    @Column(nullable = false)
    private String oauthId;
}
