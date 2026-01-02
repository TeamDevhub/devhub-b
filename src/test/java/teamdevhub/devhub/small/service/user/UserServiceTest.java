package teamdevhub.devhub.small.service.user;

import org.junit.jupiter.api.BeforeEach;
import teamdevhub.devhub.service.user.UserService;
import teamdevhub.devhub.small.mock.provider.FakeDateTimeProvider;
import teamdevhub.devhub.small.mock.provider.FakeEmailCertificationCodeProvider;
import teamdevhub.devhub.small.mock.provider.FakePasswordPolicyProvider;
import teamdevhub.devhub.small.mock.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.small.mock.repository.FakeEmailCertificationRepository;
import teamdevhub.devhub.small.mock.repository.FakeRefreshTokenRepository;
import teamdevhub.devhub.small.mock.repository.FakeUserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void init () {
        userService = new UserService(new FakeUserRepository(),
                new FakeEmailCertificationRepository(),
                new FakeRefreshTokenRepository(),
                new FakePasswordPolicyProvider(),
                new FakeUuidIdentifierProvider("a1b2c3d4e5f6g7h8i9j10k11l12m13nF"),
                new FakeDateTimeProvider(LocalDateTime.of(2025,1,1,12,0,0)));
    }


}