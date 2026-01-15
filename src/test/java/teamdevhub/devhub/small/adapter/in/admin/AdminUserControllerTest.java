package teamdevhub.devhub.small.adapter.in.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.admin.user.AdminUserController;
import teamdevhub.devhub.adapter.in.dto.response.user.UserBasicResponseDto;
import teamdevhub.devhub.adapter.in.dto.request.user.SearchUserRequestDto;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataListResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageVo;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.fake.pure.usecase.FakeAdminUserUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AdminUserControllerTest {

    private AdminUserController adminUserController;

    @BeforeEach
    void init() {
        FakeAdminUserUseCase fakeAdminUserUseCase = new FakeAdminUserUseCase();
        adminUserController = new AdminUserController(fakeAdminUserUseCase);
    }

    @Test
    @DisplayName("관리자_사용자_목록_조회를_하면_UserBasicResponseDto_리스트_페이지_정보와_READ_SUCCESS_의_코드를_확인할_수_있다")
    void returnResponseDtoListWhenFetchingAdminUserList() {
        // given
        SearchUserRequestDto searchUserRequestDto = SearchUserRequestDto.builder()
                .blocked(null)
                .keyword(null)
                .joinedFrom(null)
                .joinedTo(null)
                .build();

        int page = 0;
        int size = 10;

        // when
        ApiDataListResponseDto<UserBasicResponseDto> apiDataListResponseDto = adminUserController.list(searchUserRequestDto, page, size).getBody();

        // then
        assertThat(apiDataListResponseDto.getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(apiDataListResponseDto.getDataList()).hasSize(2);
        assertThat(apiDataListResponseDto.getDataList().get(0).getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(apiDataListResponseDto.getDataList().get(1).getEmail()).isEqualTo(TEST_EMAIL_2);

        PageVo pageVo = apiDataListResponseDto.getPagination();
        assertThat(pageVo).isNotNull();
        assertThat(pageVo.getPage()).isEqualTo(0);
        assertThat(pageVo.getSize()).isEqualTo(10);
        assertThat(pageVo.getTotalElements()).isEqualTo(2);
        assertThat(pageVo.getTotalPages()).isEqualTo(1);

        assertThat(apiDataListResponseDto.isSuccess()).isTrue();
        assertThat(apiDataListResponseDto.getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
    }
}