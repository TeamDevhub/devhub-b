package teamdevhub.devhub.api.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import teamdevhub.devhub.api.user.model.SearchUserRequestDto;
import teamdevhub.devhub.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.usecase.UserQueryUseCase;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.common.page.PageCommand;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserQueryUseCase userQueryUseCase;

    @GetMapping()
    public ResponseEntity<DataListApiResponseDto<UserBasicResponseDto>> list(@ModelAttribute SearchUserRequestDto searchUserRequestDto, @RequestParam int page, @RequestParam int size) {
        PageResult<User> pagedUserList = userQueryUseCase.listUser(searchUserRequestDto.toSearchUserCommand(), PageCommand.of(page, size));
        List<UserBasicResponseDto> userBasicResponseDtoList = pagedUserList.content().stream()
                        .map(UserBasicResponseDto::fromDomain)
                        .toList();

        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        userBasicResponseDtoList,
                        PageResponseDto.from(pagedUserList))
        );
    }
}
