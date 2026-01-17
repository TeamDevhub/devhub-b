package teamdevhub.devhub.domain.verification.vo;

import java.util.Objects;

public record VerificationTarget(VerificationType verificationType, String value) {

    public static VerificationTarget of(VerificationType verificationType, String value) {
        validate(verificationType, value);
        return new VerificationTarget(verificationType, value);
    }

    public static VerificationTarget fromEntity(VerificationType verificationType, String value) {
        return new VerificationTarget(verificationType, value);
    }

    private static void validate(VerificationType type, String value) {
        Objects.requireNonNull(type, "verificationType must not be null");
        Objects.requireNonNull(value, "value must not be null");

        switch (type) {
            case EMAIL -> {
                if (!value.matches("^[^@]+@[^@]+\\.[^@]+$")) {
                    throw new IllegalArgumentException("Invalid email format");
                }
            }
            case PHONE -> {
                if (!value.matches("^01[0-9]{8,9}$")) {
                    throw new IllegalArgumentException("Invalid phone number format");
                }
            }
            case OTP -> {
                if (value.isBlank()) {
                    throw new IllegalArgumentException("OTP cannot be blank");
                }
            }
        }
    }
}