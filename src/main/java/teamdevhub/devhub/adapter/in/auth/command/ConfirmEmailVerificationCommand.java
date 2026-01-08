package teamdevhub.devhub.adapter.in.auth.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmEmailVerificationCommand {

    private String email;
    private String code;

    public static ConfirmEmailVerificationCommand of(String email, String code) {
        return new ConfirmEmailVerificationCommand(email, code);
    }
}