package teamdevhub.devhub.small.common.mock.provider;

import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;

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
