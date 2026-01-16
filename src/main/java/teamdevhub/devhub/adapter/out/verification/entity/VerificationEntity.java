package teamdevhub.devhub.adapter.out.verification.entity;

import jakarta.persistence.*;
import lombok.*;
import teamdevhub.devhub.domain.verification.VerificationType;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
        name = "verifications",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"target_type", "target_value"}
        )
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private VerificationType targetType;

    @Column(name = "target_value", nullable = false)
    private String targetValue;

    @Column
    private String code;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private boolean verified;
}