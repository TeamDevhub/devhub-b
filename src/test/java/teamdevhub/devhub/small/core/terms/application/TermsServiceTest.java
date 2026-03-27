package teamdevhub.devhub.small.core.terms.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.terms.application.TermsService;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsAgreementRepository;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TermsServiceTest {

    private TermsService termsService;

    private FakeTermsRepository termsRepository;
    private FakeTermsAgreementRepository termsAgreementRepository;

    @BeforeEach
    void setUp() {
        termsRepository = new FakeTermsRepository();
        termsAgreementRepository = new FakeTermsAgreementRepository();
        IdentifierProvider identifierProvider = () -> java.util.UUID.randomUUID().toString();

        termsService = new TermsService(termsRepository, termsAgreementRepository,  identifierProvider);
    }

    @Test
    @DisplayName("약관을 등록하면 정상적으로 저장되고 조회된다")
    void shouldRegisterTermsSuccessfully() {
        // given
        CreateTermsCommand command = CreateTermsCommand.builder()
                .title("이용약관")
                .content("약관 내용")
                .isRequired(true)
                .build();

        // when
        termsService.registerTerms(command);

        // then
        List<Terms> result = termsService.listTerms();

        assertThat(result.size()).isEqualTo(1);
    }
}