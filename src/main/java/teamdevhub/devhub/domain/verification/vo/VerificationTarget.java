package teamdevhub.devhub.domain.verification.vo;

import java.util.Objects;

public final class VerificationTarget {

    private final VerificationType verificationType;
    private final String value;

    public VerificationTarget(
            VerificationType verificationType,
            String value,
            boolean validate
    ) {
        this.verificationType = Objects.requireNonNull(verificationType);
        this.value = Objects.requireNonNull(value);
        if (validate) {
            validate();
        }
    }

    public static VerificationTarget of(
            VerificationType type,
            String value
    ) {
        return new VerificationTarget(type, value, true);
    }

    public static VerificationTarget restore(
            VerificationType type,
            String value
    ) {
        return new VerificationTarget(type, value, false);
    }

    private void validate() {
        switch (verificationType) {
            case EMAIL -> {
                if (!value.matches("^[^@]+@[^@]+\\.[^@]+$")) {
                    throw new IllegalArgumentException();
                }
            }
            case PHONE -> {
                if (!value.matches("^01[0-9]{8,9}$")) {
                    throw new IllegalArgumentException();
                }
            }
            case OTP -> {
                if (value.isBlank()) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    public VerificationType type() {
        return verificationType;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerificationTarget that)) return false;
        return verificationType == that.verificationType
                && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(verificationType, value);
    }
}