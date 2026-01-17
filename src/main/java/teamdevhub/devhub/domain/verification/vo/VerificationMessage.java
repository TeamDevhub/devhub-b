package teamdevhub.devhub.domain.verification.vo;

import java.time.LocalDateTime;

public record VerificationMessage(String code, LocalDateTime expiredAt) {

    public static VerificationMessage of(String code, LocalDateTime expiredAt) {
        return new VerificationMessage(code, expiredAt);
    }
}
