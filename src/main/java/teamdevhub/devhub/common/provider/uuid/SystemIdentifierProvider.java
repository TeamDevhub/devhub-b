package teamdevhub.devhub.common.provider.uuid;

import java.util.UUID;

public class SystemIdentifierProvider implements IdentifierProvider {

    public String generateIdentifier() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }
}
