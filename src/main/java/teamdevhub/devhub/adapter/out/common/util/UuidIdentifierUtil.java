package teamdevhub.devhub.adapter.out.common.util;

import teamdevhub.devhub.port.out.common.IdentifierProvider;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidIdentifierUtil implements IdentifierProvider {

    @Override
    public String generate() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }
}
