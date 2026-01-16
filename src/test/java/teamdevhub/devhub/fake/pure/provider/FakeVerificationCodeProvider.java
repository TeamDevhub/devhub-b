package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.common.provider.verification.VerificationCodeProvider;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

public class FakeVerificationCodeProvider implements VerificationCodeProvider {

    private final String fixedEmailVerificationCode;

    public FakeVerificationCodeProvider(String fixedEmailVerificationCode) {
        this.fixedEmailVerificationCode = fixedEmailVerificationCode;
    }

    @Override
    public boolean supports(VerificationType type) {
        return false;
    }

    @Override
    public String generateVerificationCode() {
        return fixedEmailVerificationCode;
    }
}
