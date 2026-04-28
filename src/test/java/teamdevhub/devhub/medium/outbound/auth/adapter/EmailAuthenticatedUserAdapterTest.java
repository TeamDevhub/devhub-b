package teamdevhub.devhub.medium.outbound.auth.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.outbound.auth.adapter.EmailUserCredentialAdapter;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.outbound.auth.persistence.JpaEmailCredentialRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest
@Transactional
class EmailAuthenticatedUserAdapterTest {

    @Autowired
    private EmailUserCredentialAdapter emailUserCredentialAdapter;

    @Autowired
    private JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @BeforeEach
    void init() {
        jpaEmailCredentialRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일로_EmailUserCredential_을_조회한다")
    void findByEmail_found() {
        // given
        jpaEmailCredentialRepository.save(
                EmailCredentialEntity.builder()
                        .userGuid(TEST_USER_GUID_1)
                        .email(TEST_EMAIL_1)
                        .password(TEST_PASSWORD_1)
                        .userRole(UserRole.USER)
                        .build()
        );

        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByEmail(TEST_EMAIL_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.get().getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.get().getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("존재하지_않는_이메일로_조회하면_Optional_empty_를_반환한다")
    void findByEmail_notFound_returnsEmpty() {
        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByEmail("notexist@email.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("userGuid_로_EmailUserCredential_을_조회한다")
    void findByUserGuid_found() {
        // given
        jpaEmailCredentialRepository.save(
                EmailCredentialEntity.builder()
                        .userGuid(TEST_USER_GUID_1)
                        .email(TEST_EMAIL_1)
                        .password(TEST_PASSWORD_1)
                        .userRole(UserRole.USER)
                        .build()
        );

        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.get().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.get().getPassword()).isEqualTo(TEST_PASSWORD_1);
    }

    @Test
    @DisplayName("존재하지_않는_userGuid_로_조회하면_Optional_empty_를_반환한다")
    void findByUserGuid_notFound_returnsEmpty() {
        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByUserGuid("NOT_EXIST_GUID");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("관리자_역할의_EmailUserCredential_을_올바르게_조회한다")
    void findByEmail_adminRole_returnsCorrectRole() {
        // given
        jpaEmailCredentialRepository.save(
                EmailCredentialEntity.builder()
                        .userGuid(ADMIN_USER_GUID_1)
                        .email(ADMIN_EMAIL_1)
                        .password(ADMIN_PASSWORD_1)
                        .userRole(UserRole.ADMIN)
                        .build()
        );

        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByEmail(ADMIN_EMAIL_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(result.get().getUserGuid()).isEqualTo(ADMIN_USER_GUID_1);
    }
}
