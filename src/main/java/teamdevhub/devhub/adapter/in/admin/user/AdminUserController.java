package teamdevhub.devhub.adapter.in.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.dto.request.user.SearchUserRequestDto;
import teamdevhub.devhub.adapter.in.dto.response.user.UserBasicResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.adapter.in.vo.PageVo;
import teamdevhub.devhub.adapter.in.web.dto.response.DataListApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.admin.user.AdminUserUseCase;
import teamdevhub.devhub.port.in.common.command.PageCommand;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserUseCase adminUserUseCase;

    @GetMapping()
    public ResponseEntity<DataListApiResponseDto<UserBasicResponseDto>> list(@ModelAttribute SearchUserRequestDto searchUserRequestDto, @RequestParam int page, @RequestParam int size) {
        PageResult<User> pagedUserList = adminUserUseCase.listUser(searchUserRequestDto.toSearchUserCommand(), PageCommand.of(page, size));
        List<UserBasicResponseDto> userBasicResponseDtoList = pagedUserList.content().stream()
                        .map(UserBasicResponseDto::fromDomain)
                        .toList();

        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        userBasicResponseDtoList,
                        PageVo.from(pagedUserList))
        );
    }
}
