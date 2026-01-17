package teamdevhub.devhub.small.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.UserPositionAdapter;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.user.JpaUserPositionRepository;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.spring.persistence.user.FakeJpaUserPositionRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserPositionAdapterTest {

    private UserPositionAdapter userPositionAdapter;

    @BeforeEach
    void init() {
        JpaUserPositionRepository jpaUserPositionRepository = new FakeJpaUserPositionRepository();
        IdentifierProvider identifierProvider = new FakeUuidIdentifierProvider(TEST_USER_POSITION_GUID);
        userPositionAdapter = new UserPositionAdapter(jpaUserPositionRepository, identifierProvider);
    }

    @Test
    @DisplayName("전체_관심_포지션을_저장한다")
    void saveAll_savesPositionsCorrectly() {
        // given
        UserPosition userPosition1 = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        UserPosition userPosition2 = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);
        Set<UserPosition> positions = new HashSet<>(Set.of(userPosition1, userPosition2));

        // when
        userPositionAdapter.saveAll(positions);

        // then
        Set<UserPosition> savedUserPositions = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(savedUserPositions).hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, NEW_POSITION_CD);
    }

    @Test
    @DisplayName("관심포지션이_변경되면_모든_변경사항이_반영된다")
    void replace_mergesOldAndNewPositionsCorrectly() {
        // given
        UserPosition oldPos = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> previousPositions = new HashSet<>(Set.of(oldPos));
        userPositionAdapter.saveAll(previousPositions);

        UserPosition newPos = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);
        Set<UserPosition> currentPositions = new HashSet<>(Set.of(oldPos, newPos));

        // when
        userPositionAdapter.replace(previousPositions, currentPositions);

        // then
        Set<UserPosition> finalPositions = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(finalPositions).hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, NEW_POSITION_CD);
    }

    @Test
    @DisplayName("변경사항에서_삭제될_값으로_식별된_포지션_값은_삭제된다")
    void replace_removesDeletedPositionsCorrectly() {
        // given
        UserPosition oldPos1 = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        UserPosition oldPos2 = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);
        Set<UserPosition> previousPositions = new HashSet<>(Set.of(oldPos1, oldPos2));
        userPositionAdapter.saveAll(previousPositions);

        Set<UserPosition> currentPositions = new HashSet<>(Set.of(oldPos1));

        // when
        userPositionAdapter.replace(previousPositions, currentPositions);

        // then
        Set<UserPosition> finalPositions = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(finalPositions).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);
    }

    @Test
    @DisplayName("변경사항이_존재하지_않으면_변경되지_않는다")
    void replace_noChanges_doesNothing() {
        // given
        UserPosition userPosition = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> userPositions = new HashSet<>(Set.of(userPosition));
        userPositionAdapter.saveAll(userPositions);

        // when
        userPositionAdapter.replace(userPositions, userPositions);

        // then
        Set<UserPosition> finalPositions = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(finalPositions).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);
    }
}