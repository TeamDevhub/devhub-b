package teamdevhub.devhub.small.adapter.in.user.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

class SignupRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 올바른_데이터는_검증에_통과한다() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .username(TEST_USERNAME)
                .introduction(TEST_INTRO)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void 이메일이_비어있으면_검증에_실패한다() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email("")
                .password(TEST_PASSWORD)
                .introduction(TEST_INTRO)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("이메일은 필수입니다.");
    }

    @Test
    void 포지션이_없으면_검증에_실패한다() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .introduction(TEST_INTRO)
                .positionList(null)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("관심 포지션은 필수입니다.");
    }

    @Test
    void 포지션이_빈_값이면_검증에_실패한다() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .introduction(TEST_INTRO)
                .positionList(List.of())
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("관심 포지션은 최소 1개 이상 선택해야 합니다.");
    }

    @Test
    void 스킬이_없으면_검증에_실패한다() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .introduction(TEST_INTRO)
                .positionList(TEST_POSITION_LIST)
                .skillList(null)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("보유 스킬은 필수입니다.");
    }

    @Test
    void 스킬이_빈_값이면_검증에_실패한다() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .introduction(TEST_INTRO)
                .positionList(TEST_POSITION_LIST)
                .skillList(List.of())
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("보유 스킬은 최소 1개 이상 선택해야 합니다.");
    }
}