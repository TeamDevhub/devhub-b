package teamdevhub.devhub.domain.record.mail;

import java.time.LocalDateTime;

public record EmailCertification(String email,
                                 String code,
                                 LocalDateTime expiredAt,
                                 LocalDateTime verifiedAt) {

    public static EmailCertification of(String email, String code, LocalDateTime expiredAt, LocalDateTime verifiedAt) {
        return new EmailCertification(email, code, expiredAt, verifiedAt);
    }
}
