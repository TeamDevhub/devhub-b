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
    private String username;

    public SearchUserCommand toSearchUserCommand() {
        Boolean blocked = null;
        if ("Y".equalsIgnoreCase(this.blocked)) {
            blocked = Boolean.TRUE;
        }

        if ("N".equalsIgnoreCase(this.blocked)) {
            blocked = Boolean.FALSE;
        }

        String username = null;
        if (this.username != null && !this.username.isBlank()) {
            username = this.username.trim();
        }

        return SearchUserCommand.builder()
                .blocked(blocked)
                .joinedFrom(this.joinedFrom)
                .joinedTo(this.joinedTo)
                .username(username)
                .build();
    }
}
