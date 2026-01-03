package teamdevhub.devhub.adapter.in.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataListResponseVo;
import teamdevhub.devhub.adapter.in.common.vo.PageVo;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.port.in.user.UserUseCase;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserUseCase userUseCase;

    @GetMapping()
    public ResponseEntity<ApiDataListResponseVo<Void>> list() {
        return ResponseEntity.ok(
                ApiDataListResponseVo.successWithDataList(
                        SuccessCodeEnum.SIGNUP_SUCCESS,
                        List.of(),
                        PageVo.builder().build()));
    }
}
