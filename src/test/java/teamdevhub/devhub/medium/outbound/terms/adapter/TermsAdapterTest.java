package teamdevhub.devhub.medium.outbound.terms.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.outbound.terms.adapter.TermsAdapter;
import teamdevhub.devhub.outbound.terms.persistence.JpaTermsAgreementRepository;
import teamdevhub.devhub.outbound.terms.persistence.JpaTermsRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.TermsTestConstant.*;

@SpringBootTest
@Transactional
class TermsAdapterTest {

    @Autowired
    private TermsAdapter termsAdapter;

    @Autowired
    private JpaTermsRepository jpaTermsRepository;

    @Autowired
    private JpaTermsAgreementRepository jpaTermsAgreementRepository;

    @BeforeEach
    void init() {
        jpaTermsAgreementRepository.deleteAll();
        jpaTermsRepository.deleteAll();
    }

    private Terms sampleTerms(String guid, String title) {
        return Terms.of(guid, title, TERMS_CONTENT_1, REQUIRED, USED, NOT_DELETED);
    }

    @Test
    @DisplayName("약관을_저장하면_DB에_저장된다")
    void saveTerms_persistsToDb() {
        // given
        Terms terms = sampleTerms(TERMS_GUID_1, TERMS_TITLE_1);

        // when
        termsAdapter.saveTerms(terms);

        // then
        assertThat(jpaTermsRepository.findByTermsGuid(TERMS_GUID_1)).isPresent();
    }

    @Test
    @DisplayName("전체_약관_목록을_조회하면_저장된_모든_약관이_반환된다")
    void listTerms_returnsAllSavedTerms() {
        // given
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_2, TERMS_TITLE_2));

        // when
        List<Terms> result = termsAdapter.listTerms();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("약관_GUID로_조회하면_해당_약관을_반환한다")
    void findByTermsGuid_returnsCorrectTerms() {
        // given
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));

        // when
        Terms result = termsAdapter.findByTermsGuid(TERMS_GUID_1);

        // then
        assertThat(result.getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(result.getTitle()).isEqualTo(TERMS_TITLE_1);
    }

    @Test
    @DisplayName("존재하지_않는_GUID로_조회하면_AdapterDataException이_발생한다")
    void findByTermsGuid_notFound_throwsAdapterDataException() {
        // when, then
        assertThatThrownBy(() -> termsAdapter.findByTermsGuid("NOT_EXIST_GUID"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.NOT_EXISTED_TERMS_AGREEMENT.getMessage());
    }

    @Test
    @DisplayName("GUID_목록으로_삭제되지_않고_사용중인_약관만_조회된다")
    void findAllByTermsGuidIn_returnsOnlyActiveTerms() {
        // given — GUID_1 is active, GUID_2 is deleted
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));
        Terms deletedTerms = Terms.of(TERMS_GUID_2, TERMS_TITLE_2, TERMS_CONTENT_2, OPTIONAL, USED, DELETED);
        termsAdapter.saveTerms(deletedTerms);

        // when
        List<Terms> result = termsAdapter.findAllByTermsGuidIn(Set.of(TERMS_GUID_1, TERMS_GUID_2));

        // then — only GUID_1 should be returned (not deleted)
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTermsGuid()).isEqualTo(TERMS_GUID_1);
    }

    @Test
    @DisplayName("약관_동의_목록을_저장하면_DB에_모두_저장된다")
    void saveAll_persistsAllAgreements() {
        // given
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));

        TermsAgreement agreement = TermsAgreement.of(AGREEMENT_GUID_1, TERMS_GUID_1, USER_GUID_1, AGREED);

        // when
        termsAdapter.saveAll(List.of(agreement));

        // then
        assertThat(jpaTermsAgreementRepository.findAll()).hasSize(1);
        assertThat(jpaTermsAgreementRepository.findAll().get(0).getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(jpaTermsAgreementRepository.findAll().get(0).getUserGuid()).isEqualTo(USER_GUID_1);
        assertThat(jpaTermsAgreementRepository.findAll().get(0).isAgreed()).isTrue();
    }

    @Test
    @DisplayName("여러_약관_동의_항목을_한번에_저장할_수_있다")
    void saveAll_multipleAgreements_savesAll() {
        // given
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_1, TERMS_TITLE_1));
        termsAdapter.saveTerms(sampleTerms(TERMS_GUID_2, TERMS_TITLE_2));

        TermsAgreement agreement1 = TermsAgreement.of(AGREEMENT_GUID_1, TERMS_GUID_1, USER_GUID_1, AGREED);
        TermsAgreement agreement2 = TermsAgreement.of(AGREEMENT_GUID_2, TERMS_GUID_2, USER_GUID_1, NOT_AGREED);

        // when
        termsAdapter.saveAll(List.of(agreement1, agreement2));

        // then
        assertThat(jpaTermsAgreementRepository.findAll()).hasSize(2);
    }
}
