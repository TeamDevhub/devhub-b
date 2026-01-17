package teamdevhub.devhub.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import teamdevhub.devhub.port.out.provider.DateTimeProvider;
import teamdevhub.devhub.adapter.out.provider.time.SystemDateTimeProvider;
import teamdevhub.devhub.adapter.out.provider.password.BCryptPasswordPolicyProvider;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;
import teamdevhub.devhub.adapter.out.provider.identifier.SystemIdentifierProvider;
import teamdevhub.devhub.port.out.provider.VerificationCodeProvider;
import teamdevhub.devhub.adapter.out.provider.verification.SystemVerificationCodeProvider;
import teamdevhub.devhub.port.out.provider.PasswordPolicyProvider;

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
