package teamdevhub.devhub.domain.verification.vo;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record VerificationMessage(String code, LocalDateTime expiredAt) {

    public static VerificationMessage of(String code, LocalDateTime expiredAt) {
        return new VerificationMessage(code, expiredAt);
    }
}
