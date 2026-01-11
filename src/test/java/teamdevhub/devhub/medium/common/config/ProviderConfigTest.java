package teamdevhub.devhub.medium.common.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.common.config.ProviderConfig;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.datetime.SystemDateTimeProvider;
import teamdevhub.devhub.adapter.out.common.provider.password.BCryptPasswordPolicyProvider;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.common.provider.uuid.SystemIdentifierProvider;
import teamdevhub.devhub.common.provider.verification.EmailVerificationCodeProvider;
import teamdevhub.devhub.common.provider.verification.SystemEmailVerificationCodeProvider;
import teamdevhub.devhub.port.out.auth.PasswordPolicyProvider;

import static org.assertj.core.api.Assertions.assertThat;

public class ProviderConfigTest {

    @Test
    @DisplayName("DateTimeProvider_가_생성된다")
    void createDateTimeProvider() {
        // given
        ProviderConfig providerConfig = new ProviderConfig(passwordEncoderStub());

        // when
        DateTimeProvider dateTimeProvider = providerConfig.dateTimeProvider();

        // then
        assertThat(dateTimeProvider).isNotNull();
        assertThat(dateTimeProvider).isInstanceOf(SystemDateTimeProvider.class);
    }

    @Test
    @DisplayName("PasswordPolicyProvider_가_생성된다")
    void createPasswordPolicyProvider() {
        // given
        ProviderConfig providerConfig = new ProviderConfig(passwordEncoderStub());

        // when
        PasswordPolicyProvider passwordPolicyProvider = providerConfig.passwordPolicyProvider();

        // then
        assertThat(passwordPolicyProvider).isNotNull();
        assertThat(passwordPolicyProvider).isInstanceOf(BCryptPasswordPolicyProvider.class);
    }

    @Test
    @DisplayName("IdentifierProvider_가_생성된다")
    void createIdentifierProvider() {
        // given
        ProviderConfig providerConfig = new ProviderConfig(passwordEncoderStub());

        // when
        IdentifierProvider identifierProvider = providerConfig.identifierProvider();

        // then
        assertThat(identifierProvider).isNotNull();
        assertThat(identifierProvider).isInstanceOf(SystemIdentifierProvider.class);
    }

    @Test
    @DisplayName("EmailVerificationCodeProvider_가_생성된다")
    void createEmailVerificationCodeProvider() {
        // given
        ProviderConfig providerConfig = new ProviderConfig(passwordEncoderStub());

        // when
        EmailVerificationCodeProvider emailVerificationCodeProvider = providerConfig.emailVerificationCodeProvider();

        // then
        assertThat(emailVerificationCodeProvider).isNotNull();
        assertThat(emailVerificationCodeProvider).isInstanceOf(SystemEmailVerificationCodeProvider.class);
    }

    private PasswordEncoder passwordEncoderStub() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }
}