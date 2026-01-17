package teamdevhub.devhub.adapter.out.provider.identifier;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;

import java.util.UUID;

@Component
public class SystemIdentifierProvider implements IdentifierProvider {

    public String generateIdentifier() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }
}
