package teamdevhub.devhub.adapter.out.verification.entity;

import jakarta.persistence.*;
import lombok.*;
import teamdevhub.devhub.adapter.out.common.converter.BooleanToYNConverter;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "verifications")
public class VerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type", nullable = false)
    private VerificationType verificationType;

    @Column(name = "target_value", nullable = false)
    private String targetValue;

    @Column
    private String code;

    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean verified;
}