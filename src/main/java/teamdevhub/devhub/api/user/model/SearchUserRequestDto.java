package teamdevhub.devhub.api.user.model;

import lombok.*;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;

import java.time.LocalDateTime;

@Getter
@Setter
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
