package teamdevhub.devhub.adapter.in.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.admin.user.dto.SearchUserRequestDto;
import teamdevhub.devhub.adapter.in.common.pagination.PageCommand;
import teamdevhub.devhub.adapter.in.common.pagination.PageConverter;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataListResponseVo;
import teamdevhub.devhub.adapter.in.common.vo.PageVo;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserUseCase adminUserUseCase;

    @GetMapping()
    public ResponseEntity<ApiDataListResponseVo<AdminUserSummaryResponseDto>> list(@ModelAttribute SearchUserRequestDto searchUserRequestDto, @RequestParam int page, @RequestParam int size) {
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);
        PageCommand pageCommand = PageCommand.of(page, size);

        Page<AdminUserSummaryResponseDto> pagedUserList = adminUserUseCase.listUser(searchUserCommand, pageCommand);
        PageVo pageVo = PageConverter.toPageVo(pagedUserList);
        return ResponseEntity.ok(
                ApiDataListResponseVo.successWithDataList(SuccessCodeEnum.SIGNUP_SUCCESS,
                        pagedUserList.getContent(),
                        pageVo)
        );
    }
}
