package teamdevhub.devhub.medium.outbound.auth.adapter;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import teamdevhub.devhub.outbound.auth.adapter.RefreshTokenAdapter;
import teamdevhub.devhub.outbound.auth.adapter.entity.RefreshTokenEntity;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.outbound.auth.persistence.JpaRefreshTokenRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class RefreshTokenAdapterTest {

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
        Optional<RefreshToken> refreshToken = refreshTokenAdapter.findByUserGuid("USER_GUID_1");

        // then
        assertThat(refreshToken.get().userGuid()).isEqualTo("USER_GUID_1");
        assertThat(refreshToken.get().token()).isEqualTo("REFRESH_TOKEN_1");
    }

    @Test
    @DisplayName("리프레시 토큰이 없으면 Optional.empty 를 반환한다")
    void findByUserGuid_notExists_returnsEmpty() {
        // given
        String userGuid = "NOT_EXIST_USER";

        // when
        Optional<RefreshToken> result = refreshTokenAdapter.findByUserGuid(userGuid);

        // then
        assertThat(result).isEmpty();
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
