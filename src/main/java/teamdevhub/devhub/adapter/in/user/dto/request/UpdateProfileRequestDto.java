package teamdevhub.devhub.adapter.in.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDto {

    private String username;

    private String introduction;

    private List<String> positionList;

    private List<String> skillList;
}
