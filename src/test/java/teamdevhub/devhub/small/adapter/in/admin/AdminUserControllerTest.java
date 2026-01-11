package teamdevhub.devhub.small.adapter.in.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.admin.user.AdminUserController;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.admin.user.dto.SearchUserRequestDto;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataListResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageVo;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.fake.pure.usecase.FakeAdminUserUseCase;

import static org.assertj.core.api.Assertions.assertThat;

class AdminUserControllerTest {

    private AdminUserController adminUserController;

    @BeforeEach
    void init() {
        FakeAdminUserUseCase fakeAdminUserUseCase = new FakeAdminUserUseCase();
        adminUserController = new AdminUserController(fakeAdminUserUseCase);
    }

    @Test
    void 관리자_사용자_목록_조회를_하면_AdminUserSummaryResponseDto_리스트_페이지정보와_HTTPSTATUS_OK_를_반환한다() {
        // given
        SearchUserRequestDto requestDto = SearchUserRequestDto.builder()
                .blocked(null)
                .keyword(null)
                .joinedFrom(null)
                .joinedTo(null)
                .build();

        int page = 0;
        int size = 10;

        // when
        ApiDataListResponseDto<AdminUserSummaryResponseDto> body = adminUserController.list(requestDto, page, size).getBody();

        // then
        assertThat(body.getDataList()).hasSize(2);
        assertThat(body.getDataList().get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(body.getDataList().get(1).getEmail()).isEqualTo("user2@example.com");

        PageVo pagination = body.getPagination();
        assertThat(pagination).isNotNull();
        assertThat(pagination.getPage()).isEqualTo(0);
        assertThat(pagination.getSize()).isEqualTo(10);
        assertThat(pagination.getTotalElements()).isEqualTo(2);
        assertThat(pagination.getTotalPages()).isEqualTo(1);

        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
    }
}