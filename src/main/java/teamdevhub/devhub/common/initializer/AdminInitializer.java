package teamdevhub.devhub.common.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.user.UserUseCase;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserUseCase userUseCase;

    @Override
    public void run(String... args) {
        boolean existAdmin = userUseCase.existsByUserRole(UserRole.ADMIN);
        if (!existAdmin) {
            userUseCase.initializeAdminUser("admin@admin.co.kr", "admin1234!", "admin");
            log.info("기본 ADMIN 계정 생성됨 - ID : admin@admin.co.kr / PW : admin1234!");
        }
    }
}
