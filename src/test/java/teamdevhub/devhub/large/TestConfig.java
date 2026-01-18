package teamdevhub.devhub.large;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import teamdevhub.devhub.port.out.provider.VerificationCodeProvider;
import teamdevhub.devhub.port.out.sender.NotificationSender;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    VerificationCodeProvider verificationCodeGenerator() {
        return () -> "123456";
    }

    @Bean
    @Primary
    NotificationSender notificationSender() {
        return (to, content) -> {};
    }
}
