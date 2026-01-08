package teamdevhub.devhub.small.mock.provider;

import teamdevhub.devhub.common.provider.verification.EmailVerificationCodeProvider;

public class FakeEmailVerificationCodeProvider implements EmailVerificationCodeProvider {

    private final String fixedEmailCertificationCode;

    public FakeEmailVerificationCodeProvider(String fixedEmailCertificationCode) {
        this.fixedEmailCertificationCode = fixedEmailCertificationCode;
    }

    @Override
    public String generateEmailCertificationCode() {
        return fixedEmailCertificationCode;
    }
}
