package teamdevhub.devhub.api.terms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.user.model.SearchUserRequestDto;
import teamdevhub.devhub.api.user.model.SignupRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermsController {

}
