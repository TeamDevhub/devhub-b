package teamdevhub.devhub.medium.adapter.in.admin.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.api.user.adapter.in.controller.AdminUserController;
import teamdevhub.devhub.api.user.adapter.in.model.request.SearchUserRequestDto;
import teamdevhub.devhub.api.user.adapter.in.model.response.UserBasicResponseDto;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.shared.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.shared.web.model.response.PageResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.user.CreateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.AdminUserUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AdminUserControllerTest {

    private AdminUserController adminUserController;

    private AdminUserUseCase adminUserUseCase;

    @BeforeEach
    void init() {
        adminUserUseCase = Mockito.mock(AdminUserUseCase.class);

        adminUserController = new AdminUserController(adminUserUseCase);
    }

    @Test
    @DisplayName("관리자계정이_사용자_목록_조회_시_UserBasicResponseDto_리스트와_페이지 정보_READ_SUCCESS_코드를_반환한다")
    void returnResponseDtoListWhenFetchingAdminUserList() {
        // given
        SignupUserCommand signupUserCommand1 = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        CreateUserCommand createUserCommand1 = CreateUserCommand.generalUserCreateCommand(
                signupUserCommand1, TEST_USER_GUID_1, TEST_PASSWORD_1);

        User testUser1 = User.createGeneralUser(createUserCommand1);

        SignupUserCommand signupUserCommand2 = SignupUserCommand.builder()
                .email(TEST_EMAIL_2)
                .password(TEST_PASSWORD_2)
                .username(TEST_USERNAME_2)
                .introduction(TEST_INTRO_2)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        CreateUserCommand createUserCommand2 = CreateUserCommand.generalUserCreateCommand(
                signupUserCommand2, TEST_USER_GUID_2, TEST_PASSWORD_2);

        User testUser2 = User.createGeneralUser(createUserCommand2);

        PageResult<User> pageResult = PageResult.of(
                List.of(testUser1, testUser2),
                0,
                10,
                2
        );

        Mockito.when(adminUserUseCase.listUser(any(SearchUserCommand.class), any(PageCommand.class)))
                .thenReturn(pageResult);

        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .blocked(null)
                .keyword(null)
                .joinedFrom(null)
                .joinedTo(null)
                .build();

        // when
        ResponseEntity<DataListApiResponseDto<UserBasicResponseDto>> response = adminUserController.list(requestDto, 0, 10);

        // then
        DataListApiResponseDto<UserBasicResponseDto> body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());

        assertThat(body.getDataList()).hasSize(2);
        assertThat(body.getDataList().get(0).getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(body.getDataList().get(1).getEmail()).isEqualTo(TEST_EMAIL_2);

        PageResponseDto pageResponseDto = body.getPagination();
        assertThat(pageResponseDto.getPage()).isEqualTo(0);
        assertThat(pageResponseDto.getSize()).isEqualTo(10);
        assertThat(pageResponseDto.getTotalElements()).isEqualTo(2);
        assertThat(pageResponseDto.getTotalPages()).isEqualTo(1);

        Mockito.verify(adminUserUseCase).listUser(any(SearchUserCommand.class), any(PageCommand.class));
    }
}
