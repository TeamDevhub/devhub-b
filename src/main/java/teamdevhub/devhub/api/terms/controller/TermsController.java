package teamdevhub.devhub.api.terms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.user.model.SignupRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermsController {

//    private final TermsFacade termsFacade;
//
//    @GetMapping
//    public ResponseEntity<DataApiResponseDto<Void>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
//        termsFacade.list(signupRequestDto.toSignupCommand());
//        return ResponseEntity.ok(
//                DataApiResponseDto.successWithoutData(
//                        SuccessCode.SIGNUP_SUCCESS
//                )
//        );
//    }
}
