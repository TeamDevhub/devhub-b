package teamdevhub.devhub.port.in.authentication.command;

import lombok.Builder;

@Builder
public record LoginCommand(String email, String password) {}
