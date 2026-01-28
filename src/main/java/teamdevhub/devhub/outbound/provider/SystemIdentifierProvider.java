package teamdevhub.devhub.outbound.provider;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;

import java.util.UUID;

@Component
public class SystemIdentifierProvider implements IdentifierProvider {

    public String generateIdentifier() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }
}
