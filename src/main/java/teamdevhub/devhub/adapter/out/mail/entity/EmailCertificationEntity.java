package teamdevhub.devhub.adapter.out.mail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "email_certifications")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailCertificationEntity {

    @Id @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 6)
    private String code;

    private LocalDateTime expiredAt;

    private LocalDateTime verifiedAt;

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }

    public boolean isVerified() {
        return verifiedAt != null;
    }

    public void verify(LocalDateTime now) {
        this.code = null;
        this.verifiedAt = now;
    }
}
