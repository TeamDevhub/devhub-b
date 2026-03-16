package teamdevhub.devhub.api.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.api.terms.model.TermsAgreementRequestDto;
import teamdevhub.devhub.api.web.validator.RegexPattern;
import teamdevhub.devhub.api.web.validator.RegexMatch;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.VerificationType;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "이메일은 필수입니다")
    @RegexMatch(RegexPattern.AUTH_EMAIL)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String password;

    @NotBlank(message = "사용자명은 필수입니다")
    @RegexMatch(RegexPattern.USERNAME)
    private String username;

    private String introduction;

    @NotNull(message = "관심 포지션은 필수입니다")
    @Size(min = 1, message = "관심 포지션은 최소 1개 이상 선택해야 합니다")
    private List<@NotBlank String> positionList;

    @NotNull(message = "보유 스킬은 필수입니다")
    @Size(min = 1, message = "보유 스킬은 최소 1개 이상 선택해야 합니다")
    private List<@NotBlank String> skillList;

    @NotNull(message = "약관 동의 정보는 필수입니다")
    @Size(min = 1, message = "약관 동의는 최소 1개 이상 필요합니다")
    private List<TermsAgreementRequestDto> termsAgreementList;

    public SignupUserCommand toSignupCommand() {
        return SignupUserCommand.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .introduction(this.introduction)
                .positionList(this.positionList)
                .skillList(this.skillList)
                .termsAgreementItemList(
                        this.termsAgreementList.stream()
                                .map(TermsAgreementRequestDto::toTermsAgreementItem)
                                .toList())
                .verificationTarget(VerificationTarget.of(VerificationType.EMAIL, this.email))
                .build();
    }
}