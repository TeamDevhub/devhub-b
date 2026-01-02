package teamdevhub.devhub.port.in.mail;

import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailCertificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailCertificationRequestDto;

public interface EmailCertificationUseCase {
    void sendEmailCertificationCode(EmailCertificationRequestDto emailCertificationRequestDto);
    void confirmEmailCertificationCode(ConfirmEmailCertificationCommand confirmEmailCertificationCommand);
    boolean isVerified(String email);
    void delete(String email);
}
