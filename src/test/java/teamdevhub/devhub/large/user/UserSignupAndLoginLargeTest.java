//package teamdevhub.devhub.large.user;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.context.annotation.Import;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.*;
//import org.testcontainers.containers.MySQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import teamdevhub.devhub.adapter.in.dto.request.auth.LoginRequestDto;
//import teamdevhub.devhub.adapter.in.dto.request.user.SignupRequestDto;
//import teamdevhub.devhub.adapter.in.dto.request.verification.ConfirmVerificationRequestDto;
//import teamdevhub.devhub.adapter.in.dto.request.verification.IssueVerificationRequestDto;
//import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;
//import teamdevhub.devhub.adapter.in.dto.response.user.SignupResponseDto;
//import teamdevhub.devhub.adapter.in.dto.response.user.UserDetailResponseDto;
//import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataResponseDto;
//import teamdevhub.devhub.common.enums.SuccessCode;
//import teamdevhub.devhub.domain.verification.vo.VerificationType;
//import teamdevhub.devhub.large.TestConfig;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static teamdevhub.devhub.constant.UserTestConstant.*;
//import static teamdevhub.devhub.constant.UserTestConstant.TEST_SKILL_LIST;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Import(TestConfig.class)
//@Testcontainers
//public class UserSignupAndLoginLargeTest {
//
//    @Container
//    static MySQLContainer<?> mysql =
//            new MySQLContainer<>("mysql:8.0")
//                    .withDatabaseName("test")
//                    .withUsername("test")
//                    .withPassword("test");
//
//    @Autowired
//    TestRestTemplate testRestTemplate;
//
//    @Test
//    @DisplayName("이메일_인증_회원가입_로그인_프로필조회_E2E_테스트")
//    void 이메일_인증부터_회원가입_로그인_프로필조회까지() {
//        testRestTemplate.postForEntity(
//                "/auth/email-verification",
//                new IssueVerificationRequestDto(VerificationType.EMAIL, TEST_EMAIL_1),
//                Void.class
//        );
//
//        testRestTemplate.postForEntity(
//                "/auth/email-verification/confirm",
//                new ConfirmVerificationRequestDto(VerificationType.EMAIL, TEST_EMAIL_1, "123456"),
//                Void.class
//        );
//
//        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
//                .email(TEST_EMAIL_1)
//                .password(TEST_PASSWORD_1)
//                .username(TEST_USERNAME_1)
//                .introduction(TEST_INTRO_1)
//                .positionList(TEST_POSITION_LIST)
//                .skillList(TEST_SKILL_LIST)
//                .build();
//
//        ResponseEntity<ApiDataResponseDto<SignupResponseDto>> signupResponse =
//                testRestTemplate.exchange(
//                        "/user/signup",
//                        HttpMethod.POST,
//                        new HttpEntity<>(signupRequestDto),
//                        new ParameterizedTypeReference<>() {}
//                );
//
//        assertThat(signupResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
//
//        ResponseEntity<ApiDataResponseDto<TokenResponseDto>> loginResponse =
//                testRestTemplate.exchange(
//                        "/auth/login",
//                        HttpMethod.POST,
//                        new HttpEntity<>(new LoginRequestDto(TEST_EMAIL_1, TEST_PASSWORD_1)),
//                        new ParameterizedTypeReference<>() {}
//                );
//
//        String accessToken = loginResponse.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.AUTHORIZATION, accessToken);
//        List<String> setCookies = loginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
//
//        assertThat(setCookies).anyMatch(cookie ->
//                cookie.contains("refreshToken"));
//
//        ResponseEntity<ApiDataResponseDto<UserDetailResponseDto>> response =
//                testRestTemplate.exchange(
//                        "/user/profile",
//                        HttpMethod.GET,
//                        new HttpEntity<>(headers),
//                        new ParameterizedTypeReference<>() {}
//                );
//
//        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
//    }
//}
