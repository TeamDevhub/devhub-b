package teamdevhub.devhub.small.common.mock.provider;

import teamdevhub.devhub.common.provider.verification.EmailVerificationCodeProvider;

public class FakeEmailVerificationCodeProvider implements EmailVerificationCodeProvider {

    private final String fixedEmailVerificationCode;

    public FakeEmailVerificationCodeProvider(String fixedEmailVerificationCode) {
        this.fixedEmailVerificationCode = fixedEmailVerificationCode;
    }

    @Override
    public String generateEmailVerificationCode() {
        return fixedEmailVerificationCode;
    }
}
