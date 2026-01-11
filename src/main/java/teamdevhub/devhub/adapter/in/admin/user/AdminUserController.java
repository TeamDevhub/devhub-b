package teamdevhub.devhub.adapter.in.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.admin.user.dto.SearchUserRequestDto;
import teamdevhub.devhub.port.in.common.command.PageCommand;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataListResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageVo;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserUseCase adminUserUseCase;

    @GetMapping()
    public ResponseEntity<ApiDataListResponseDto<AdminUserSummaryResponseDto>> list(@ModelAttribute SearchUserRequestDto searchUserRequestDto, @RequestParam int page, @RequestParam int size) {
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);
        PageCommand pageCommand = PageCommand.of(page, size);

        PageResult<AdminUserSummaryResponseDto> pagedUserList = adminUserUseCase.listUser(searchUserCommand, pageCommand);
        return ResponseEntity.ok(
                ApiDataListResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        pagedUserList.content(),
                        PageVo.from(pagedUserList))
        );
    }
}
