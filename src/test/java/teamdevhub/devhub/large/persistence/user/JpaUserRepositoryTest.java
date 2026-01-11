package teamdevhub.devhub.large.persistence.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.out.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

@SpringBootTest
public class JpaUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 회원이_저장된다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        User savedUser = userRepository.saveNewUser(user);

        // then
        assertThat(savedUser.getUserGuid()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(TEST_EMAIL);
    }
}
