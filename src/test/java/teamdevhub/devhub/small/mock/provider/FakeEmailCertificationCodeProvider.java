package teamdevhub.devhub.small.mock.provider;

import teamdevhub.devhub.port.out.common.EmailCertificationCodeProvider;

public class FakeEmailCertificationCodeProvider implements EmailCertificationCodeProvider {

    private final String fixedEmailCertificationCode;

    public FakeEmailCertificationCodeProvider(String fixedEmailCertificationCode) {
        this.fixedEmailCertificationCode = fixedEmailCertificationCode;
    }

    @Override
    public String generateEmailCertificationCode() {
        return fixedEmailCertificationCode;
    }
}
