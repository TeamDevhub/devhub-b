package teamdevhub.devhub.core.notification.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.notification.port.in.NotificationQueryUseCase;
import teamdevhub.devhub.core.notification.port.in.NotificationUseCase;
import teamdevhub.devhub.core.notification.port.in.facade.model.NotificationResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationQueryUseCase notificationQueryUseCase;
    private final NotificationUseCase notificationUseCase;

    public DataListApiResponseDto<NotificationResponseDto> getNotificationList(String userGuid) {

        List<NotificationResponseDto> notificationList = notificationQueryUseCase.getNotificationList(userGuid).stream()
                .map(NotificationResponseDto::fromDomain)
                .toList();

        return DataListApiResponseDto.successWithDataList(
                SuccessCode.READ_SUCCESS,
                notificationList,
                null
        );
    }

    public DataApiResponseDto<Void> checkedNotification(String notificationGuid) {

        notificationUseCase.checkedNotification(notificationGuid);

        return DataApiResponseDto.successWithoutData(
                SuccessCode.UPDATE_SUCCESS
        );
    }
}
