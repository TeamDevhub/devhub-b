package teamdevhub.devhub.medium.common.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.medium.mock.constant.TestConstant.TEST_PASSWORD;

public class PasswordCryptoConfigTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void 비밀번호를_인코딩하면_원본과_일치여부를_검증할_수_있다() {
        // given
        String rawPassword = TEST_PASSWORD;

        // when
        String encoded = passwordEncoder.encode(rawPassword);

        // then
        assertThat(encoded).isNotNull();
        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
        assertThat(passwordEncoder.matches("wrongPassword", encoded)).isFalse();
    }
}
