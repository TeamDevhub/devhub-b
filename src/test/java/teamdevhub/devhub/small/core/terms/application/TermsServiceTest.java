package teamdevhub.devhub.small.core.terms.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.terms.application.TermsService;
import teamdevhub.devhub.core.terms.domain.Terms;
import teamdevhub.devhub.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.fake.pure.application.port.out.terms.FakeTermsRepository;

import static org.assertj.core.api.Assertions.assertThat;

class TermsServiceTest {

    private TermsService termsService;
    private FakeTermsRepository termsRepository;

    @BeforeEach
    void setUp() {
        termsRepository = new FakeTermsRepository();
        IdentifierProvider identifierProvider = () -> java.util.UUID.randomUUID().toString();

        termsService = new TermsService(termsRepository, identifierProvider);
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
        PageResult<Terms> result = termsService.listTerms(new PageCommand(0, 10));

        assertThat(result.content()).hasSize(1);
        assertThat(result.totalElements()).isEqualTo(1);
        assertThat(result.totalPages()).isEqualTo(1);
        assertThat(result.first()).isTrue();
        assertThat(result.last()).isTrue();
    }

    @Test
    @DisplayName("약관 목록 조회 시 페이징이 정상적으로 동작한다")
    void shouldReturnPagedTermsList() {
        // given
        for (int i = 0; i < 15; i++) {
            CreateTermsCommand command = CreateTermsCommand.builder()
                    .title("약관 " + i)
                    .content("내용 " + i)
                    .isRequired(i % 2 == 0)
                    .build();

            termsService.registerTerms(command);
        }

        // when
        PageResult<Terms> result = termsService.listTerms(new PageCommand(0, 10));

        // then
        assertThat(result.content()).hasSize(10);
        assertThat(result.totalElements()).isEqualTo(15);
        assertThat(result.totalPages()).isEqualTo(2);
        assertThat(result.first()).isTrue();
        assertThat(result.last()).isFalse();
    }
}