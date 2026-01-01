package teamdevhub.devhub.adapter.in.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

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