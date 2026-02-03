package teamdevhub.devhub.api.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequestDto {

    private String blocked;
    private LocalDateTime joinedFrom;
    private LocalDateTime joinedTo;
    private String keyword;

    public SearchUserCommand toSearchUserCommand() {
        Boolean blocked = null;
        if ("Y".equalsIgnoreCase(this.blocked)) {
            blocked = Boolean.TRUE;
        }

        if ("N".equalsIgnoreCase(this.blocked)) {
            blocked = Boolean.FALSE;
        }

        String keyword = null;
        if (this.keyword != null && !this.keyword.isBlank()) {
            keyword = this.keyword.trim();
        }

        return SearchUserCommand.builder()
                .blocked(blocked)
                .joinedFrom(this.joinedFrom)
                .joinedTo(this.joinedTo)
                .keyword(keyword)
                .build();
    }
}
