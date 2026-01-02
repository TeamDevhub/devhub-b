package teamdevhub.devhub.small.mock.provider;

import teamdevhub.devhub.port.out.common.IdentifierProvider;

public class FakeUuidIdentifierProvider implements IdentifierProvider {

    private final String fixedUuidValue;

    public FakeUuidIdentifierProvider(String fixedUuidValue) {
        this.fixedUuidValue = fixedUuidValue;
    }

    @Override
    public String generateIdentifier() {
        return fixedUuidValue;
    }
}
