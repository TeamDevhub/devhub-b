package teamdevhub.devhub.adapter.out.common.util;

import teamdevhub.devhub.port.out.common.EmailCertificationCodeProvider;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class EmailCertificationCodeUtil implements EmailCertificationCodeProvider {

    private final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generateEmailCertificationCode() {
        int CODE_LENGTH = 6;
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }
}