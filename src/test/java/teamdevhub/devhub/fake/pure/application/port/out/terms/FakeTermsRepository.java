package teamdevhub.devhub.fake.pure.application.port.out.terms;

import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.out.TermsRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FakeTermsRepository implements TermsRepository {

    private final Map<String, Terms> store = new ConcurrentHashMap<>();

    @Override
    public List<Terms> listTerms() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void saveTerms(Terms terms) {
        store.put(terms.getTermsGuid(), terms);
    }

    @Override
    public List<Terms> findAllByTermsGuidIn(Set<String> termsGuids) {
        return termsGuids.stream()
                .map(store::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Terms findByTermsGuid(String termsGuid) {
        return store.get(termsGuid);
    }
}
