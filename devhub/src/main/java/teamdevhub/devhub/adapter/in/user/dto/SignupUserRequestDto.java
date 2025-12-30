package teamdevhub.devhub.adapter.in.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupUserRequestDto {

    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}