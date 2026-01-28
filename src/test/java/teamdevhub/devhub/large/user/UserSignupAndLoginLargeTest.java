package teamdevhub.devhub.large.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import teamdevhub.devhub.api.auth.model.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.api.auth.model.request.IssueVerificationRequestDto;
import teamdevhub.devhub.api.auth.model.request.LoginRequestDto;
import teamdevhub.devhub.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.api.user.model.request.SignupRequestDto;
import teamdevhub.devhub.api.user.model.response.UserDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.large.TestConfig;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
public class UserSignupAndLoginLargeTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("이메일_인증_회원가입_로그인_프로필조회_E2E_테스트")
    void fromVerificationToGetProfile() {
        testRestTemplate.postForEntity(
                "/auth/verification/email",
                new IssueVerificationRequestDto("email", TEST_EMAIL_1),
                Void.class
        );

        testRestTemplate.postForEntity(
                "/auth/verification/email/confirm",
                new ConfirmVerificationRequestDto("email", TEST_EMAIL_1, "123456"),
                Void.class
        );

        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();
        ResponseEntity<DataApiResponseDto<Void>> signupResponse = testRestTemplate.exchange(
                "/user/signup",
                HttpMethod.POST,
                new HttpEntity<>(signupRequestDto),
                new ParameterizedTypeReference<>() {}
        );
        assertThat(signupResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<DataApiResponseDto<TokenResponseDto>> loginResponse = testRestTemplate.exchange(
                "/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(new LoginRequestDto(TEST_EMAIL_1, TEST_PASSWORD_1)),
                new ParameterizedTypeReference<>() {}
        );
        String accessToken = loginResponse.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, accessToken);
        List<String> setCookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(setCookies).anyMatch(cookie -> cookie.contains("refreshToken"));

        ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> updateProfileResponse = testRestTemplate.exchange(
                "/user/profile",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );
        assertThat(updateProfileResponse.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
    }
}
