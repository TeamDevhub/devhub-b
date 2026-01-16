package teamdevhub.devhub.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.common.provider.datetime.SystemDateTimeProvider;
import teamdevhub.devhub.adapter.out.common.provider.password.BCryptPasswordPolicyProvider;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.common.provider.uuid.SystemIdentifierProvider;
import teamdevhub.devhub.common.provider.verification.VerificationCodeProvider;
import teamdevhub.devhub.common.provider.verification.SystemVerificationCodeProvider;
import teamdevhub.devhub.port.out.auth.PasswordPolicyProvider;

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
        return new BCryptPasswordPolicyProvider(passwordEncoder);
    }

    @Bean
    public IdentifierProvider identifierProvider() {
        return new SystemIdentifierProvider();
    }

    @Bean
    public VerificationCodeProvider emailVerificationCodeProvider() {
        return new SystemVerificationCodeProvider();
    }
}
