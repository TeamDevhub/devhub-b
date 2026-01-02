package teamdevhub.devhub.adapter.in.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
    private String introduction;
}