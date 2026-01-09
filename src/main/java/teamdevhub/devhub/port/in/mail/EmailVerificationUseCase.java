package teamdevhub.devhub.port.in.mail;

import teamdevhub.devhub.port.in.mail.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;

public interface EmailVerificationUseCase {
    void sendEmailVerification(EmailVerificationRequestDto emailVerificationRequestDto);
    void confirmEmailVerification(ConfirmEmailVerificationCommand confirmEmailVerificationCommand);
    boolean isVerified(String email);
}
