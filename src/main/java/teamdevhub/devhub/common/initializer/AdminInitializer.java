package teamdevhub.devhub.common.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.in.auth.AuthUserUseCase;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AuthUserUseCase authUserUseCase;

    @Override
    public void run(String... args) {
        authUserUseCase.initializeAdminUser("admin@admin.co.kr", "admin1234!", "admin");
        log.info("기본 ADMIN 계정 생성됨 - ID : admin@admin.co.kr / PW : admin1234!");
    }
}
