package teamdevhub.devhub.small.common.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.common.config.ProviderConfig;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.datetime.SystemDateTimeProvider;
import teamdevhub.devhub.common.provider.password.PasswordPolicyProvider;
import teamdevhub.devhub.common.provider.password.SystemPasswordPolicyProvider;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.common.provider.uuid.SystemIdentifierProvider;
import teamdevhub.devhub.common.provider.verification.EmailVerificationCodeProvider;
import teamdevhub.devhub.common.provider.verification.SystemEmailVerificationCodeProvider;

import static org.assertj.core.api.Assertions.assertThat;

public class ProviderConfigTest {

    @Test
    void 날짜_시간_프로바이더가_생성된다() {
        // given
        ProviderConfig providerConfig = new ProviderConfig(passwordEncoderStub());

        // when
        DateTimeProvider dateTimeProvider = providerConfig.dateTimeProvider();

        // then
        assertThat(dateTimeProvider).isNotNull();
        assertThat(dateTimeProvider).isInstanceOf(SystemDateTimeProvider.class);
    }

    @Test
    void 비밀번호_정책_프로바이더가_생성된다() {
        // given
        ProviderConfig providerConfig = new ProviderConfig(passwordEncoderStub());

        // when
        PasswordPolicyProvider passwordPolicyProvider = providerConfig.passwordPolicyProvider();

        // then
        assertThat(passwordPolicyProvider).isNotNull();
        assertThat(passwordPolicyProvider).isInstanceOf(SystemPasswordPolicyProvider.class);
    }

    @Test
    void 식별자_프로바이더가_생성된다() {
        // given
        ProviderConfig providerConfig = new ProviderConfig(passwordEncoderStub());

        // when
        IdentifierProvider identifierProvider = providerConfig.identifierProvider();

        // then
        assertThat(identifierProvider).isNotNull();
        assertThat(identifierProvider).isInstanceOf(SystemIdentifierProvider.class);
    }

    @Test
    void 이메일_인증_코드_프로바이더가_생성된다() {
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