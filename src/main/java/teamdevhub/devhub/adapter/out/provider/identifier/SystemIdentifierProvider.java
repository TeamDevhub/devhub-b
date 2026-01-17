package teamdevhub.devhub.adapter.out.provider.identifier;

import teamdevhub.devhub.port.out.provider.IdentifierProvider;

import java.util.UUID;

public class SystemIdentifierProvider implements IdentifierProvider {

    public String generateIdentifier() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }
}
