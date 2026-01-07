package teamdevhub.devhub.adapter.in.admin.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserRequestDto {

    private String blocked;

    private LocalDateTime joinedFrom;

    private LocalDateTime joinedTo;

    private String keyword;
}
