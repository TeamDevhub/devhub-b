package teamdevhub.devhub.adapter.in.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.UserBasicResponseDto;
import teamdevhub.devhub.adapter.in.admin.user.dto.SearchUserRequestDto;
import teamdevhub.devhub.port.in.common.command.PageCommand;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataListResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageVo;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserUseCase adminUserUseCase;

    @GetMapping()
    public ResponseEntity<ApiDataListResponseDto<UserBasicResponseDto>> list(@ModelAttribute SearchUserRequestDto searchUserRequestDto, @RequestParam int page, @RequestParam int size) {
        SearchUserCommand searchUserCommand = SearchUserCommand.fromSearchUserRequestDto(searchUserRequestDto);
        PageCommand pageCommand = PageCommand.of(page, size);

        PageResult<User> pagedUserList = adminUserUseCase.listUser(searchUserCommand, pageCommand);
        List<UserBasicResponseDto> userBasicResponseDtoList = pagedUserList.content().stream()
                        .map(UserBasicResponseDto::fromDomain)
                        .toList();

        return ResponseEntity.ok(
                ApiDataListResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        userBasicResponseDtoList,
                        PageVo.from(pagedUserList))
        );
    }
}
