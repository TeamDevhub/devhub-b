package teamdevhub.devhub.common.provider.uuid;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidIdentifierUtil implements IdentifierProvider {

    @Override
    public String generateIdentifier() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }
}
