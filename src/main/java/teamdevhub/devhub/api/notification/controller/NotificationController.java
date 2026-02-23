package teamdevhub.devhub.api.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.notification.port.in.facade.NotificationFacade;
import teamdevhub.devhub.core.notification.port.in.facade.model.NotificationResponseDto;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
	
	private final NotificationFacade notificationFacade;

	@GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<NotificationResponseDto>> getNotificationList(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(notificationFacade.getNotificationList(authenticatedUser.userGuid()));
    }

    @PutMapping("/checked")
    public ResponseEntity<DataApiResponseDto<Void>> checkedNotification(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(notificationFacade.checkedNotification(authenticatedUser.userGuid()));
    }
}
