package teamdevhub.devhub.small.core.terms.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.terms.application.TermsService;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreementItem;
import teamdevhub.devhub.core.terms.port.in.command.AgreeTermsCommand;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsAgreementRepository;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.TermsTestConstant.*;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

class TermsServiceTest {

    private TermsService termsService;

    private FakeTermsRepository termsRepository;
    private FakeTermsAgreementRepository termsAgreementRepository;

    @BeforeEach
    void setUp() {
        termsRepository = new FakeTermsRepository();
        termsAgreementRepository = new FakeTermsAgreementRepository();
        IdentifierProvider identifierProvider = () -> UUID.randomUUID().toString();

        termsService = new TermsService(termsRepository, termsAgreementRepository, identifierProvider);
    }

    @Test
    @DisplayName("약관을_등록하면_정상적으로_저장되고_조회된다")
    void registerTerms_savesTermsSuccessfully() {
        // given
        CreateTermsCommand command = CreateTermsCommand.builder()
                .title(TERMS_TITLE_1)
                .content(TERMS_CONTENT_1)
                .isRequired(REQUIRED)
                .isUsed(USED)
                .build();

        // when
        termsService.registerTerms(command);

        // then
        List<Terms> result = termsService.listTerms();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo(TERMS_TITLE_1);
    }

    @Test
    @DisplayName("약관이_없으면_빈_목록을_반환한다")
    void listTerms_whenEmpty_returnsEmptyList() {
        // when
        List<Terms> result = termsService.listTerms();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("여러_약관을_등록하면_모두_조회된다")
    void listTerms_whenMultipleTermsRegistered_returnsAll() {
        // given
        termsService.registerTerms(CreateTermsCommand.builder()
                .title(TERMS_TITLE_1).content(TERMS_CONTENT_1).isRequired(REQUIRED).isUsed(USED).build());
        termsService.registerTerms(CreateTermsCommand.builder()
                .title(TERMS_TITLE_2).content(TERMS_CONTENT_2).isRequired(OPTIONAL).isUsed(USED).build());

        // when
        List<Terms> result = termsService.listTerms();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("유효한_약관에_동의하면_동의_내역이_저장된다")
    void saveTermsAgreement_validTerms_agreementSaved() {
        // given
        Terms terms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, REQUIRED, USED, NOT_DELETED);
        termsRepository.saveTerms(terms);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, AGREED)))
                .build();

        // when
        termsService.saveTermsAgreement(command);

        // then
        assertThat(termsAgreementRepository.findAll()).hasSize(1);
        assertThat(termsAgreementRepository.findAll().get(0).getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(termsAgreementRepository.findAll().get(0).isAgreed()).isTrue();
    }

    @Test
    @DisplayName("동의_목록에_없는_약관_GUID가_포함되면_예외가_발생한다")
    void saveTermsAgreement_termsNotFound_throwsBusinessRuleException() {
        // given — repository is empty, so the guid won't be found
        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, AGREED)))
                .build();

        // when, then
        assertThatThrownBy(() -> termsService.saveTermsAgreement(command))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.UNKNOWN_FAIL.getMessage());
    }

    @Test
    @DisplayName("삭제된_약관에_동의하면_도메인_예외가_발생한다")
    void saveTermsAgreement_deletedTerms_throwsDomainRuleException() {
        // given
        Terms deletedTerms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, REQUIRED, USED, DELETED);
        termsRepository.saveTerms(deletedTerms);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, AGREED)))
                .build();

        // when, then
        assertThatThrownBy(() -> termsService.saveTermsAgreement(command))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.INVALID_TERMS.getMessage());
    }

    @Test
    @DisplayName("필수_약관에_동의하지_않으면_도메인_예외가_발생한다")
    void saveTermsAgreement_requiredTermsNotAgreed_throwsDomainRuleException() {
        // given
        Terms requiredTerms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, REQUIRED, USED, NOT_DELETED);
        termsRepository.saveTerms(requiredTerms);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, NOT_AGREED)))
                .build();

        // when, then
        assertThatThrownBy(() -> termsService.saveTermsAgreement(command))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.INVALID_TERMS_AGREEMENT.getMessage());
    }

    @Test
    @DisplayName("선택_약관은_동의하지_않아도_저장된다")
    void saveTermsAgreement_optionalTermsNotAgreed_saved() {
        // given
        Terms optionalTerms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, OPTIONAL, USED, NOT_DELETED);
        termsRepository.saveTerms(optionalTerms);

        AgreeTermsCommand command = AgreeTermsCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .termsAgreementItemList(List.of(new TermsAgreementItem(TERMS_GUID_1, NOT_AGREED)))
                .build();

        // when
        termsService.saveTermsAgreement(command);

        // then
        assertThat(termsAgreementRepository.findAll()).hasSize(1);
        assertThat(termsAgreementRepository.findAll().get(0).isAgreed()).isFalse();
    }
}
