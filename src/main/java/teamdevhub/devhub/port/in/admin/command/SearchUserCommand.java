package teamdevhub.devhub.port.in.admin.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.adapter.in.admin.user.dto.SearchUserRequestDto;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SearchUserCommand {

    private final Boolean blocked;

    private final LocalDateTime joinedFrom;

    private final LocalDateTime joinedTo;

    private final String keyword;

    public static SearchUserCommand fromSearchUserRequestDto(SearchUserRequestDto searchUserRequestDto) {
        Boolean blocked = null;
        if ("Y".equalsIgnoreCase(searchUserRequestDto.getBlocked())) {
            blocked = Boolean.TRUE;
        }
        if ("N".equalsIgnoreCase(searchUserRequestDto.getBlocked())) {
            blocked = Boolean.FALSE;
        }
        LocalDateTime joinedFrom = searchUserRequestDto.getJoinedFrom();
        LocalDateTime joinedTo = searchUserRequestDto.getJoinedTo();

        String keyword = null;
        if (searchUserRequestDto.getKeyword() != null && !searchUserRequestDto.getKeyword().isBlank()) {
            keyword = searchUserRequestDto.getKeyword().trim();
        }

        return new SearchUserCommand(blocked, joinedFrom, joinedTo, keyword);
    }
}
