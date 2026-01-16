package teamdevhub.devhub.domain.verification.vo;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VerificationMessage {

    private final String code;
    private final LocalDateTime expiredAt;

    public VerificationMessage(String code, LocalDateTime expiredAt) {
        this.code = code;
        this.expiredAt = expiredAt;
    }

    public static VerificationMessage of(
            String code,
            LocalDateTime expiredAt
    ) {
        return new VerificationMessage(code, expiredAt);
    }
}
