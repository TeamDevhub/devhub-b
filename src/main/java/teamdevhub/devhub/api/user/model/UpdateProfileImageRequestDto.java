package teamdevhub.devhub.api.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.user.port.in.command.UpdateProfileImageCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileImageRequestDto {

    private String fileGuid;

    public UpdateProfileImageCommand toUpdateProfileImageCommand(String userGuid) {
        return UpdateProfileImageCommand.builder()
                .userGuid(userGuid)
                .fileGuid(this.fileGuid)
                .build();
    }
}
