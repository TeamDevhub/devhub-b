package teamdevhub.devhub.adapter.in.auth.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmEmailCertificationCommand {

    private String email;
    private String code;

    public static ConfirmEmailCertificationCommand of(String email, String code) {
        return new ConfirmEmailCertificationCommand(email, code);
    }
}