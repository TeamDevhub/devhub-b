package teamdevhub.devhub.small.adapter.out.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.auth.RefreshTokenAdapter;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.vo.auth.RefreshToken;
import teamdevhub.devhub.fake.spring.persistence.auth.FakeJpaRefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

class RefreshTokenAdapterTest {

    private static final String TOKEN_1 = "refresh-token-1";
    private static final String TOKEN_2 = "refresh-token-2";

    private RefreshTokenAdapter refreshTokenAdapter;

    @BeforeEach
    void init() {
        FakeJpaRefreshTokenRepository fakeJpaRepository = new FakeJpaRefreshTokenRepository();
        refreshTokenAdapter = new RefreshTokenAdapter(fakeJpaRepository);
    }

    @Test
    @DisplayName("리프레시_토큰이_없으면_새로_저장된다")
    void saveRefreshTokenIfNotExists() {
        // given
        RefreshToken refreshToken = RefreshToken.of(TEST_USER_GUID_1, TOKEN_1);

        // when
        refreshTokenAdapter.save(refreshToken);

        // then
        RefreshToken foundRefreshToken = refreshTokenAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(foundRefreshToken.token()).isEqualTo(TOKEN_1);
    }

    @Test
    @DisplayName("기존_리프레시_토큰이_있으면_rotate_된다")
    void rotateIfRefreshTokenExists() {
        // given
        refreshTokenAdapter.save(RefreshToken.of(TEST_USER_GUID_1, TOKEN_1));

        // when
        refreshTokenAdapter.save(RefreshToken.of(TEST_USER_GUID_1, TOKEN_2));

        // then
        RefreshToken foundRefreshToken = refreshTokenAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(foundRefreshToken.token()).isEqualTo(TOKEN_2);
    }

    @Test
    @DisplayName("리프레시_토큰이_없으면_예외가_발생한다")
    void throwIfRefreshTokenNotExists() {
        // then
        assertThatThrownBy(
                // given, when
                () -> refreshTokenAdapter.findByUserGuid(TEST_USER_GUID_1))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("리프레시_토큰을_삭제할_수_있다")
    void deleteRefreshToken() {
        // given
        refreshTokenAdapter.save(RefreshToken.of(TEST_USER_GUID_1, TOKEN_1));

        // when
        refreshTokenAdapter.deleteByUserGuid(TEST_USER_GUID_1);

        // then
        assertThatThrownBy(() -> refreshTokenAdapter.findByUserGuid(TEST_USER_GUID_1))
                .isInstanceOf(AdapterDataException.class);
    }
}