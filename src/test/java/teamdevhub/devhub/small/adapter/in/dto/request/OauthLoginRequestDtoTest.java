package teamdevhub.devhub.small.adapter.in.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.request.OauthLoginRequestDto;
import teamdevhub.devhub.port.in.oauth.command.ResolveOauthUserCommand;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class OauthLoginRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("tempToken_이_정상적이면_OauthLoginCommand_로_변환된다")
    void convertOauthLoginRequestDtoToCommand() {
        // given
        OauthLoginRequestDto oauthLoginRequestDto = OauthLoginRequestDto.builder()
                .tempToken("temp-token-123")
                .build();

        // when
        ResolveOauthUserCommand resolveOauthUserCommand = oauthLoginRequestDto.toCommand();

        // then
        assertThat(resolveOauthUserCommand.tempToken()).isEqualTo("temp-token-123");
    }

    @Test
    @DisplayName("tempToken_이_null_이거나_빈값이면_검증에_실패한다")
    void validationFailsWhenTempTokenIsNullOrBlank() {
        // given
        OauthLoginRequestDto oauthLoginRequestDto = new OauthLoginRequestDto();

        // when
        Set<ConstraintViolation<OauthLoginRequestDto>> violations = validator.validate(oauthLoginRequestDto);

        // then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("필수값이 누락되었습니다.");
    }
}