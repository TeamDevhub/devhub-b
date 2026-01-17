package teamdevhub.devhub.port.in.auth.command;

import lombok.Builder;

@Builder
public record LoginCommand(String email, String password) {}
