package teamdevhub.devhub.small.adapter.in.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.request.OauthSignupRequestDto;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OauthSignupRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든_필드가_올바르면_OauthSignupCommand_로_변환된다")
    void convertOauthSignupRequestDtoToCommand() {
        // given
        OauthSignupRequestDto oauthSignupRequestDto = OauthSignupRequestDto.builder()
                .tempToken("temp-token-123")
                .password("Password123!")
                .username("testUser")
                .introduction("안녕하세요")
                .positionList(List.of("BACKEND"))
                .skillList(List.of("JAVA", "SPRING"))
                .build();

        // when
        OauthSignupCommand oauthSignupCommand = oauthSignupRequestDto.toCommand();

        // then
        assertThat(oauthSignupCommand.tempToken()).isEqualTo("temp-token-123");
        assertThat(oauthSignupCommand.password()).isEqualTo("Password123!");
        assertThat(oauthSignupCommand.username()).isEqualTo("testUser");
        assertThat(oauthSignupCommand.introduction()).isEqualTo("안녕하세요");
        assertThat(oauthSignupCommand.positionList()).containsExactly("BACKEND");
        assertThat(oauthSignupCommand.skillList()).containsExactly("JAVA", "SPRING");
    }

    @Test
    @DisplayName("필수_필드가_null_이거나_빈값이면_검증에_실패한다")
    void validationFailsForOauthSignupRequestDto() {
        // given
        OauthSignupRequestDto oauthSignupRequestDto = new OauthSignupRequestDto();

        // when
        Set<ConstraintViolation<OauthSignupRequestDto>> violations = validator.validate(oauthSignupRequestDto);

        // then
        assertThat(violations).isNotEmpty();

        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("TEMP 토큰은 필수입니다.",
                        "비밀번호는 필수입니다.",
                        "관심 포지션은 필수입니다.",
                        "보유 스킬은 필수입니다.");
    }

    @Test
    @DisplayName("positionList_와_skillList_가_빈_리스트면_검증에_실패한다")
    void validationFailsWhenListsAreEmpty() {
        // given
        OauthSignupRequestDto oauthSignupRequestDto = OauthSignupRequestDto.builder()
                .tempToken("temp-token-123")
                .password("Password123!")
                .positionList(List.of())
                .skillList(List.of())
                .build();

        // when
        Set<ConstraintViolation<OauthSignupRequestDto>> violations = validator.validate(oauthSignupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("관심 포지션은 최소 1개 이상 선택해야 합니다.",
                        "보유 스킬은 최소 1개 이상 선택해야 합니다.");
    }
}
