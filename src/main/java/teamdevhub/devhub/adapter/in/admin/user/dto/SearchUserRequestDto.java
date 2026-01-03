package teamdevhub.devhub.adapter.in.admin.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SearchUserRequestDto {

    private String blocked;

    private LocalDateTime joinedFrom;
    private LocalDateTime joinedTo;

    private String keyword;
}
