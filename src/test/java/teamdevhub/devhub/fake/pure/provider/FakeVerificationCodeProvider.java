package teamdevhub.devhub.fake.pure.provider;

import teamdevhub.devhub.port.out.provider.VerificationCodeProvider;

public class FakeVerificationCodeProvider implements VerificationCodeProvider {

    private final String fixedEmailVerificationCode;

    public FakeVerificationCodeProvider(String fixedEmailVerificationCode) {
        this.fixedEmailVerificationCode = fixedEmailVerificationCode;
    }

    @Override
    public String generateVerificationCode() {
        return fixedEmailVerificationCode;
    }
}
