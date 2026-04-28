package teamdevhub.devhub.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Admin - User", description = "관리자 사용자 관리 API")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserQueryUseCase userQueryUseCase;

    @Operation(summary = "사용자 목록 조회 (관리자)", description = "관리자가 검색 조건으로 전체 사용자 목록을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping()
    public ResponseEntity<DataListApiResponseDto<UserBasicResponseDto>> list(
            @ModelAttribute SearchUserRequestDto searchUserRequestDto,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam int size) {
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
