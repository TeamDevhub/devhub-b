package teamdevhub.devhub.port.in.auth.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record LoginCommand(String email, String password) {}
