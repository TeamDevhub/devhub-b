package teamdevhub.devhub.adapter.out.common.util;

import teamdevhub.devhub.port.out.provider.IdentifierProvider;
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
