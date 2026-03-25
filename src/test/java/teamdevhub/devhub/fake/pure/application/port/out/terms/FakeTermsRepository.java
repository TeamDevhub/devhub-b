package teamdevhub.devhub.fake.pure.application.port.out.terms;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.out.TermsRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FakeTermsRepository implements TermsRepository {

    private final Map<String, Terms> store = new ConcurrentHashMap<>();

    @Override
    public PageResult<Terms> listTerms(PageCommand pageCommand) {
        List<Terms> allTerms = new ArrayList<>(store.values());

        int start = pageCommand.page() * pageCommand.size();
        int end = Math.min(start + pageCommand.size(), allTerms.size());

        List<Terms> content = allTerms.subList(start, end);

        return PageResult.of(
                content,
                pageCommand.page(),
                pageCommand.size(),
                allTerms.size()
        );
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
