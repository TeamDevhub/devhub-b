package teamdevhub.devhub.small.mock.provider;

import teamdevhub.devhub.port.out.provider.IdentifierProvider;

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
