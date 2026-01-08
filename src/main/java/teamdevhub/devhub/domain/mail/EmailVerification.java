package teamdevhub.devhub.domain.mail;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EmailVerification {

    private final String email;
    private final String code;
    private final LocalDateTime expiredAt;
    private boolean verified;

    @Builder
    private EmailVerification(
            String email,
            String code,
            LocalDateTime expiredAt,
            boolean verified
    ) {
        this.email = email;
        this.code = code;
        this.expiredAt = expiredAt;
        this.verified = verified;
    }

    public static EmailVerification issue(
            String email,
            String code,
            LocalDateTime expiredAt
    ) {
        return EmailVerification.builder()
                .email(email)
                .code(code)
                .expiredAt(expiredAt)
                .verified(false)
                .build();
    }

    public static EmailVerification from(
            String email,
            String code,
            LocalDateTime expiredAt,
            boolean verified
    ) {
        return EmailVerification.builder()
                .email(email)
                .code(code)
                .expiredAt(expiredAt)
                .verified(verified)
                .build();
    }

    public boolean verify(String inputCode, LocalDateTime now) {
        if (isExpired(now)) {
            return false;
        }

        if (!code.equals(inputCode)) {
            return false;
        }

        this.verified = true;
        return true;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }
}
