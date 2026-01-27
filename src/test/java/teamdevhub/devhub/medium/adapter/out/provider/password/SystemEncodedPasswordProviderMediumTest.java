package teamdevhub.devhub.medium.adapter.out.provider.password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.adapter.out.common.provider.password.SystemEncodedPasswordProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_PASSWORD_1;

class SystemEncodedPasswordProviderMediumTest {

    SystemEncodedPasswordProvider systemPasswordPolicyProvider;

    @BeforeEach
    void init() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        systemPasswordPolicyProvider = new SystemEncodedPasswordProvider(passwordEncoder);
    }

    @Test
    @DisplayName("암호화된 비밀번호가 BCrypt 형식으로 생성되는지 확인한다")
    void encodedPasswordHasValidBCryptFormat() {
        // given
        String hashedPassword = systemPasswordPolicyProvider.encode(TEST_PASSWORD_1);

        // then
        assertThat(
                // when
                hashedPassword.startsWith("$2a$") ||
                        hashedPassword.startsWith("$2b$") ||
                        hashedPassword.startsWith("$2y$")
        ).isTrue();
    }

    @Test
    @DisplayName("비밀번호를 암호화하면 일치 검증이 정상적으로 동작한다")
    void encryptPasswordMatchesSuccessfully() {
        // given
        String hashedPassword = systemPasswordPolicyProvider.encode(TEST_PASSWORD_1);

        // when, then
        assertThat(hashedPassword).isNotNull();
        assertThat(systemPasswordPolicyProvider.matches(TEST_PASSWORD_1, hashedPassword)).isTrue();
        assertThat(systemPasswordPolicyProvider.matches("wrongPassword", hashedPassword)).isFalse();
    }

}