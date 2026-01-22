package teamdevhub.devhub.common.initializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.in.user.command.SignupAdminCommand;
import teamdevhub.devhub.port.in.user.usecase.UserSignupUseCase;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserSignupUseCase userSignupUseCase;

    @Override
    public void run(String... args) {
        SignupAdminCommand signupAdminCommand = new SignupAdminCommand(null, "admin@admin.co.kr", "admin1234!", "admin", "", null, null, null);
        userSignupUseCase.initializeAdminUser(signupAdminCommand);
        log.info("기본 ADMIN 계정 생성됨 - ID : admin@admin.co.kr / PW : admin1234!");
    }
}
