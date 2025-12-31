package teamdevhub.devhub.common.util;

import teamdevhub.devhub.port.out.common.IdentifierGeneratorPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidIdentifierGeneratorUtil implements IdentifierGeneratorPort {

    @Override
    public String generate() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }
}
