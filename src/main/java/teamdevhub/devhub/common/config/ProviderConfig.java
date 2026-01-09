package teamdevhub.devhub.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.datetime.SystemDateTimeProvider;
import teamdevhub.devhub.common.provider.password.PasswordPolicyProvider;
import teamdevhub.devhub.common.provider.password.SystemPasswordPolicyProvider;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.common.provider.uuid.SystemIdentifierProvider;
import teamdevhub.devhub.common.provider.verification.EmailVerificationCodeProvider;
import teamdevhub.devhub.common.provider.verification.SystemEmailVerificationCodeProvider;

@Configuration
@RequiredArgsConstructor
public class ProviderConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return new SystemDateTimeProvider();
    }

    @Bean
    public PasswordPolicyProvider passwordPolicyProvider() {
        return new SystemPasswordPolicyProvider(passwordEncoder);
    }

    @Bean
    public IdentifierProvider identifierProvider() {
        return new SystemIdentifierProvider();
    }

    @Bean
    public EmailVerificationCodeProvider emailVerificationCodeProvider() {
        return new SystemEmailVerificationCodeProvider();
    }
}
