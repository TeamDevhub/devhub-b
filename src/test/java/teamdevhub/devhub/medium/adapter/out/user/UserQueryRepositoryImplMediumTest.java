package teamdevhub.devhub.medium.adapter.out.user;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.user.UserQueryRepository;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest
@Transactional
class UserQueryRepositoryImplMediumTest {

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void init() {
        String randomGuid1 = UUID.randomUUID().toString().replace("-", "");
        String randomGuid2 = UUID.randomUUID().toString().replace("-", "");
        UserEntity userEntity1 = UserEntity.builder()
                .userGuid(randomGuid1)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(false)
                .deleted(false)
                .introduction(TEST_INTRO_1)
                .lastLoginDt(LocalDateTime.now().minusDays(1))
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .userGuid(randomGuid2)
                .email(TEST_EMAIL_2)
                .password(TEST_PASSWORD_2)
                .username(TEST_USERNAME_2)
                .userRole(UserRole.USER)
                .mannerDegree(50.0)
                .blocked(true)
                .deleted(false)
                .introduction(TEST_INTRO_2)
                .lastLoginDt(LocalDateTime.now().minusDays(1))
                .build();

        entityManager.persist(userEntity1);
        entityManager.persist(userEntity2);
        entityManager.flush();
    }

    @Test
    @DisplayName("차단된_사용자_검색조건이_적용되면_차단된_사용자만_조회된다")
    void listUser_withBlockedTrueFilter_returnsCorrectResults() {
        // given
        SearchUserCommand searchUserCommand = new SearchUserCommand(
            true,
            null,
            null,
            null);

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<UserEntity> pagedUserEntity = userQueryRepository.listUser(searchUserCommand, pageable);

        // then
        assertThat(pagedUserEntity.getContent()).hasSize(1);
        assertThat(pagedUserEntity.getContent().get(0).getUsername()).isEqualTo(TEST_USERNAME_2);
    }
}
