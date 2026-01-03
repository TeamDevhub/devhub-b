package teamdevhub.devhub.adapter.in.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.admin.user.dto.SearchUserRequestDto;
import teamdevhub.devhub.adapter.in.common.pagination.PageCommand;
import teamdevhub.devhub.adapter.in.common.pagination.PageConverter;
import teamdevhub.devhub.adapter.in.common.pagination.PageRequestDto;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataListResponseVo;
import teamdevhub.devhub.adapter.in.common.vo.PageVo;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserUseCase adminUserUseCase;

    @GetMapping()
    public ResponseEntity<ApiDataListResponseVo<AdminUserSummaryResponseDto>> list(@ModelAttribute SearchUserRequestDto searchUserRequestDto, @ModelAttribute PageRequestDto pageRequestDto) {
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);
        PageCommand pageCommand = PageCommand.of(pageRequestDto);
        Page<User> pagedUserList = adminUserUseCase.listUser(searchUserCommand, pageCommand);
        List<AdminUserSummaryResponseDto> dataList = PageConverter.toList(pagedUserList, AdminUserSummaryResponseDto::fromDomain);
        PageVo pageVo = PageConverter.toPageVo(pagedUserList);
        return ResponseEntity.ok(
                ApiDataListResponseVo.successWithDataList(SuccessCodeEnum.SIGNUP_SUCCESS,
                        dataList,
                        pageVo)
        );
    }
}
