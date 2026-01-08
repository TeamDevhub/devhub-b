package teamdevhub.devhub.port.in.mail;

import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;

public interface EmailVerificationUseCase {
    void sendEmailCertificationCode(EmailVerificationRequestDto emailVerificationRequestDto);
    void confirmEmailCertificationCode(ConfirmEmailVerificationCommand confirmEmailVerificationCommand);
    boolean isVerified(String email);
}
