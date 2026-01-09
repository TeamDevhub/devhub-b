package teamdevhub.devhub.small.adapter.out.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.auth.RefreshTokenAdapter;
import teamdevhub.devhub.adapter.out.exception.AdapterDataException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.vo.auth.RefreshToken;
import teamdevhub.devhub.small.mock.persistence.auth.FakeJpaRefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RefreshTokenAdapterTest {

    private RefreshTokenAdapter refreshTokenAdapter;
    private FakeJpaRefreshTokenRepository fakeJpaRepository;

    private static final String USER_GUID = "USER_GUID";
    private static final String TOKEN_1 = "refresh-token-1";
    private static final String TOKEN_2 = "refresh-token-2";

    @BeforeEach
    void init() {
        fakeJpaRepository = new FakeJpaRefreshTokenRepository();
        refreshTokenAdapter = new RefreshTokenAdapter(fakeJpaRepository);
    }

    @Test
    void 리프레시_토큰이_없으면_새로_저장된다() {
        // given
        RefreshToken refreshToken = RefreshToken.of(USER_GUID, TOKEN_1);

        // when
        refreshTokenAdapter.save(refreshToken);

        // then
        RefreshToken result = refreshTokenAdapter.findByUserGuid(USER_GUID);
        assertThat(result.token()).isEqualTo(TOKEN_1);
    }

    @Test
    void 기존_리프레시_토큰이_있으면_rotate된다() {
        // given
        refreshTokenAdapter.save(RefreshToken.of(USER_GUID, TOKEN_1));

        // when
        refreshTokenAdapter.save(RefreshToken.of(USER_GUID, TOKEN_2));

        // then
        RefreshToken result = refreshTokenAdapter.findByUserGuid(USER_GUID);
        assertThat(result.token()).isEqualTo(TOKEN_2);
    }

    @Test
    void 리프레시_토큰이_없으면_예외가_발생한다() {
        // then
        assertThatThrownBy(
                // given, when
                () -> refreshTokenAdapter.findByUserGuid(USER_GUID))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    void 리프레시_토큰을_삭제할_수_있다() {
        // given
        refreshTokenAdapter.save(RefreshToken.of(USER_GUID, TOKEN_1));

        // when
        refreshTokenAdapter.deleteByUserGuid(USER_GUID);

        // then
        assertThatThrownBy(() -> refreshTokenAdapter.findByUserGuid(USER_GUID))
                .isInstanceOf(AdapterDataException.class);
    }
}