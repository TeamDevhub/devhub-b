package teamdevhub.devhub.api.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.board.model.SearchBoardRequestDto;
import teamdevhub.devhub.api.user.model.UpdatePasswordRequestDto;
import teamdevhub.devhub.api.user.model.UpdateProfileImageRequestDto;
import teamdevhub.devhub.api.user.model.UpdateProfileRequestDto;
import teamdevhub.devhub.api.web.model.request.PageRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.board.port.in.Facade.BoardFacade;
import teamdevhub.devhub.core.board.port.in.Facade.model.BoardSummaryResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.core.project.port.in.facade.model.UserProjectResponseDto;
import teamdevhub.devhub.core.user.port.in.facade.UserProfileFacade;
import teamdevhub.devhub.core.user.port.in.facade.UserWithdrawFacade;
import teamdevhub.devhub.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.core.user.port.in.facade.model.UserDetailResponseDto;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileFacade userProfileFacade;
    private final UserWithdrawFacade userWithdrawFacade;
    private final BoardFacade boardFacade;
    private final ProjectFacade projectFacade;

    @GetMapping()
    public ResponseEntity<DataApiResponseDto<UserBasicResponseDto>> getUserInfo(@LoginUser UserCredential userCredential) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        userProfileFacade.getUserInfo(userCredential.userGuid())
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> getProfile(@LoginUser UserCredential userCredential) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        userProfileFacade.getCurrentUserProfile(userCredential.userGuid())
                )
        );
    }

    @PostMapping("/profile/image")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfileImage(@Valid @RequestBody UpdateProfileImageRequestDto updateProfileImageRequestDto, @LoginUser UserCredential userCredential) {
        userProfileFacade.updateProfileImage(updateProfileImageRequestDto.toUpdateProfileImageCommand(userCredential.userGuid()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto, @LoginUser UserCredential userCredential) {
        userProfileFacade.updateProfile(updateProfileRequestDto.toUpdateProfileCommand(userCredential.userGuid()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

//    @PutMapping("/profile/password")
//    public ResponseEntity<DataApiResponseDto<Void>> updatePassword(@Valid @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto, @LoginUser UserCredential userCredential) {
//        userProfileFacade.updatePassword(updatePasswordRequestDto.toUpdatePasswordCommand(userCredential.userGuid()));
//        return ResponseEntity.ok(
//                DataApiResponseDto.successWithoutData(
//                        SuccessCode.UPDATE_SUCCESS
//                )
//        );
//    }

    @DeleteMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> withdraw(@LoginUser UserCredential userCredential) {
        userWithdrawFacade.withdraw(userCredential.userGuid());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.USER_DELETE_SUCCESS
                )
        );
    }
    
    @GetMapping("/profile/boards")
    public ResponseEntity<DataListApiResponseDto<BoardSummaryResponseDto>> getUserListBoard(@ModelAttribute SearchBoardRequestDto searchBoardRequestDto, PageRequestDto pageRequestDto, 
    		@LoginUser UserCredential userCredential) {
    	return ResponseEntity.ok(boardFacade.listBoard(searchBoardRequestDto.toCommand(userCredential.userGuid()), PageCommand.of((pageRequestDto.getPage()), pageRequestDto.getSize())));
    }
    
    @GetMapping("/projects")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserProjects(@RequestParam("page") int page, @RequestParam("size") int size, 
    		@LoginUser UserCredential userCredential) {
        return ResponseEntity.ok(
        		DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        projectFacade.getUserProjects(userCredential.userGuid(), PageCommand.of(page, size))
                )
        );
    }
    
    @GetMapping("/projects/likes")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserLikeProjects(@RequestParam("page") int page, @RequestParam("size") int size, 
    		@LoginUser UserCredential userCredential) {
        return ResponseEntity.ok(
        		DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        projectFacade.getUserLikeProjects(userCredential.userGuid(), PageCommand.of(page, size))
                )
        );
    }
    
    @GetMapping("/projects/applications")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserApplyProjects(@RequestParam("page") int page, @RequestParam("size") int size, 
    		@LoginUser UserCredential userCredential) {
        return ResponseEntity.ok(
        		DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        projectFacade.getUserApplyProjects(userCredential.userGuid(), PageCommand.of(page, size))
                )
        );
    }
}