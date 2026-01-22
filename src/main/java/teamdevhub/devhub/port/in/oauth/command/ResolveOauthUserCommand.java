package teamdevhub.devhub.port.in.oauth.command;

import lombok.Builder;

@Builder
public record ResolveOauthUserCommand(String tempToken) { }
