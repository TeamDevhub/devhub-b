package teamdevhub.devhub.small.adapter.out.auth.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.*;

class RefreshTokenEntityTest {

    @Test
    @DisplayName("RefreshTokenEntity_를_생성자로_생성하면_userGuid_와_token_이_올바르게_설정된다")
    void testConstructorAndGetters() {
        // given
        String token = "token-abc";

        // when
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userGuid(TEST_GUID_1)
                .token(token)
                .build();

        // then
        assertThat(refreshTokenEntity.getUserGuid()).isEqualTo(TEST_GUID_1);
        assertThat(refreshTokenEntity.getToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("팩토리_메서드_of_를_통해_RefreshTokenEntity_를_생성하면_필드_값이_올바르게_설정된다")
    void testStaticFactoryOf() {
        // given
        String token = "token-def";

        // when
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(TEST_GUID_1, token);

        // then
        assertThat(refreshTokenEntity.getUserGuid()).isEqualTo(TEST_GUID_1);
        assertThat(refreshTokenEntity.getToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("rotate_메서드를_호출하면_token_값이_새로운_값으로_변경된다")
    void testRotateToken() {
        // given
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.of(TEST_GUID_1, "old-token");
        String newToken = "new-token";

        // when
        refreshTokenEntity.rotate(newToken);

        // then
        assertThat(refreshTokenEntity.getToken()).isEqualTo(newToken);
    }
}