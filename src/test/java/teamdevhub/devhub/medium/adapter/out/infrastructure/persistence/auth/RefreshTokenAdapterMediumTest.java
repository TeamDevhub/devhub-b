package teamdevhub.devhub.medium.adapter.out.infrastructure.persistence.auth;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import teamdevhub.devhub.adapter.out.auth.token.RefreshTokenAdapter;
import teamdevhub.devhub.adapter.out.auth.token.RefreshTokenEntity;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.auth.JpaRefreshTokenRepository;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.auth.RefreshToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RefreshTokenAdapterMediumTest {

    @Autowired
    private RefreshTokenAdapter refreshTokenAdapter;

    @Autowired
    private JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @BeforeEach
    void init() {
        jpaRefreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("리프레시_토큰을_저장한다")
    void saveRefreshToken() {
        // given
        RefreshToken refreshToken = RefreshToken.of("USER_GUID_1", "REFRESH_TOKEN_1");

        // when
        refreshTokenAdapter.save(refreshToken);

        // then
        RefreshTokenEntity refreshTokenEntity = jpaRefreshTokenRepository.findByUserGuid("USER_GUID_1").orElseThrow();

        assertThat(refreshTokenEntity.getUserGuid()).isEqualTo("USER_GUID_1");
        assertThat(refreshTokenEntity.getToken()).isEqualTo("REFRESH_TOKEN_1");
    }

    @Test
    @DisplayName("이미_존재하는_리프레시_토큰이_있다면_토큰을_변경시킨다")
    void saveRefreshToken_rotate() {
        // given
        jpaRefreshTokenRepository.save(RefreshTokenEntity.of("USER_GUID_1", "OLD_REFRESH_TOKEN"));
        RefreshToken refreshToken = RefreshToken.of("USER_GUID_1", "NEW_REFRESH_TOKEN");

        // when
        refreshTokenAdapter.save(refreshToken);

        // then
        RefreshTokenEntity refreshTokenEntity = jpaRefreshTokenRepository.findByUserGuid("USER_GUID_1").orElseThrow();

        assertThat(refreshTokenEntity.getToken()).isEqualTo("NEW_REFRESH_TOKEN");
    }

    @Test
    @DisplayName("사용자_식별키로_리프레시_토큰을_조회한다")
    void findByUserGuid() {
        // given
        jpaRefreshTokenRepository.save(RefreshTokenEntity.of("USER_GUID_1", "REFRESH_TOKEN_1"));

        // when
        RefreshToken refreshToken = refreshTokenAdapter.findByUserGuid("USER_GUID_1");

        // then
        assertThat(refreshToken.userGuid()).isEqualTo("USER_GUID_1");
        assertThat(refreshToken.token()).isEqualTo("REFRESH_TOKEN_1");
    }

    @Test
    @DisplayName("리프레시_토큰이_없으면_예외를_발생시킨다")
    void findByUserGuid_notExists_throwsException() {
        // given
        String userGuid = "NOT_EXIST_USER";

        // when then
        assertThatThrownBy(
                () -> refreshTokenAdapter.findByUserGuid(userGuid))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("사용자_식별키로_리프레시_토큰을_삭제한다")
    void deleteByUserGuid() {
        // given
        jpaRefreshTokenRepository.save(RefreshTokenEntity.of("USER_GUID_1", "REFRESH_TOKEN_1"));

        // when
        refreshTokenAdapter.deleteByUserGuid("USER_GUID_1");

        // then
        assertThat(jpaRefreshTokenRepository.findByUserGuid("USER_GUID_1")).isEmpty();
    }
}
