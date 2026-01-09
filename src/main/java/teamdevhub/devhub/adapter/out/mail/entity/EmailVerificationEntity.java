package teamdevhub.devhub.adapter.out.mail.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.adapter.out.common.converter.BooleanToYNConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationEntity {

    @Id @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 6)
    private String code;

    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean verified;
}
