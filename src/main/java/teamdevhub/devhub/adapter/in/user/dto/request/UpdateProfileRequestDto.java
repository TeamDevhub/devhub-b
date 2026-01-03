package teamdevhub.devhub.adapter.in.user.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class UpdateProfileRequestDto {

    private String userGuid;

    private String username;

    private String introduction;

    private List<String> positionList;

    private List<String> skillList;
}
