package teamdevhub.devhub.adapter.in.auth;

import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailCertificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.ConfirmEmailCertificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.EmailCertificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.TokenResponseDto;
import teamdevhub.devhub.adapter.in.common.vo.ApiResponseVo;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.mail.EmailCertificationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final EmailCertificationUseCase emailCertificationUseCase;

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponseVo<TokenResponseDto>> refresh(@CookieValue("refreshToken") String refreshToken) {
        return ResponseEntity.ok(
                ApiResponseVo.successWithData(
                        SuccessCodeEnum.CREATE_SUCCESS,
                        authUseCase.refreshAccessToken(refreshToken)
                )
        );
    }

    @PostMapping("/email-certification")
    public ResponseEntity<ApiResponseVo<Void>> sendEmailCertificationCode(@Valid @RequestBody EmailCertificationRequestDto emailCertificationRequestDto) {
        emailCertificationUseCase.sendEmailCertificationCode(emailCertificationRequestDto);
        return ResponseEntity.ok(
                ApiResponseVo.successWithoutData(
                        SuccessCodeEnum.EMAIL_CERTIFICATION_SENT
                )
        );
    }

    @PostMapping("/email-certification/confirm")
    public ResponseEntity<ApiResponseVo<Void>> confirmEmailCertificationCode(@Valid @RequestBody ConfirmEmailCertificationRequestDto confirmEmailCertificationRequestDto) {
        ConfirmEmailCertificationCommand confirmEmailCertificationCommand = ConfirmEmailCertificationCommand.of(confirmEmailCertificationRequestDto.getEmail(), confirmEmailCertificationRequestDto.getCode());
        emailCertificationUseCase.confirmEmailCertificationCode(confirmEmailCertificationCommand);
        return ResponseEntity.ok(
                ApiResponseVo.successWithoutData(
                        SuccessCodeEnum.EMAIL_CERTIFICATION_SUCCESS
                )
        );
    }
}
