package teamdevhub.devhub.adapter.in.user.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UpdateProfileCommand {

    private String userGuid;

    private String username;

    private String introduction;

    private List<String> positionList;

    private List<String> skillList;

    public static UpdateProfileCommand fromUpdateProfileRequestDto(UpdateProfileRequestDto updateProfileRequestDto, String userGuid) {
        return UpdateProfileCommand.builder()
                .userGuid(userGuid)
                .username(updateProfileRequestDto.getUsername())
                .introduction(updateProfileRequestDto.getIntroduction())
                .positionList(updateProfileRequestDto.getPositionList())
                .skillList(updateProfileRequestDto.getSkillList())
                .build();
    }
}
