package teamdevhub.devhub.constant;

import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

public final class VerificationTestConstant {

    private VerificationTestConstant() {}

    public static final String TEST_EMAIL = "user1@example.com";
    public static final String EMAIL_CODE = "123456";
    public static final String UNVERIFIED_EMAIL = "unverified@example.com";

    public static final VerificationTarget VERIFICATION_TARGET = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL);
}
