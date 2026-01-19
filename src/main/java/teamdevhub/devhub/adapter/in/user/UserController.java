package teamdevhub.devhub.adapter.in.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.dto.request.user.SignupRequestDto;
import teamdevhub.devhub.adapter.in.dto.request.user.UpdateProfileRequestDto;
import teamdevhub.devhub.adapter.in.dto.response.user.SignupResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.user.UserDetailResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.adapter.in.web.resolver.LoginUser;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.SIGNUP_SUCCESS,
                        SignupResponseDto.fromDomain(userFacade.signup(signupRequestDto.toSignupCommand()))
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> getProfile(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        UserDetailResponseDto.fromDomain(userFacade.getUserDetailProfile(authenticatedUser.userGuid()))
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        userFacade.updateProfile(updateProfileRequestDto.toUpdateProfileCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

    @DeleteMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> withdraw(@LoginUser AuthenticatedUser authenticatedUser) {
        userFacade.withdrawUser(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.USER_DELETE_SUCCESS
                )
        );
    }
}