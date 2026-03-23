package teamdevhub.devhub.small.core.terms.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.terms.application.TermsAgreementService;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.core.terms.domain.TermsAgreementItem;
import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsAgreementRepository;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TermsAgreementServiceTest {

    private TermsAgreementService termsAgreementService;

    private FakeTermsRepository termsRepository;
    private FakeTermsAgreementRepository agreementRepository;
    private FakeUuidIdentifierProvider identifierProvider;

    @BeforeEach
    void setUp() {
        termsRepository = new FakeTermsRepository();
        agreementRepository = new FakeTermsAgreementRepository();
        identifierProvider = new FakeUuidIdentifierProvider(UUID.randomUUID().toString());

        termsAgreementService = new TermsAgreementService(
                termsRepository,
                agreementRepository,
                identifierProvider
        );
    }

    @Test
    @DisplayName("약관 동의 시 모든 약관에 대한 동의 정보가 저장된다")
    void shouldSaveAllAgreements() {
        // given
        Terms t1 = Terms.createTerms(
                CreateTermsCommand.builder()
                        .title("약관1")
                        .content("내용1")
                        .isRequired(true)
                        .isUsed(true)
                        .isDeleted(false)
                        .build(),
                "t1"
        );

        Terms t2 = Terms.createTerms(
                CreateTermsCommand.builder()
                        .title("약관2")
                        .content("내용2")
                        .isRequired(false)
                        .isUsed(true)
                        .isDeleted(false)
                        .build(),
                "t2"
        );

        termsRepository.saveTerms(t1);
        termsRepository.saveTerms(t2);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid("user1")
                .termsAgreementItemList(List.of(
                        new TermsAgreementItem("t1", true),
                        new TermsAgreementItem("t2", false)
                ))
                .build();

        // when
        termsAgreementService.agreeTerms(command);

        // then
        List<TermsAgreement> result = agreementRepository.findAll();

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(TermsAgreement::isAgreed)
                .containsExactlyInAnyOrder(true, false);
    }

    @Test
    @DisplayName("존재하지 않는 약관이 포함되면 예외가 발생한다")
    void shouldThrowExceptionWhenTermsNotExist() {
        // given
        Terms t1 = Terms.createTerms(
                CreateTermsCommand.builder()
                        .title("약관1")
                        .content("내용1")
                        .isRequired(true)
                        .build(),
                "t1"
        );

        termsRepository.saveTerms(t1);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid("user1")
                .termsAgreementItemList(List.of(
                        new TermsAgreementItem("t1", true),
                        new TermsAgreementItem("t2", false)
                ))
                .build();

        // when & then
        assertThatThrownBy(() -> termsAgreementService.agreeTerms(command))
                .isInstanceOf(BusinessRuleException.class);
    }
}
