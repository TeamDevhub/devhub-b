package teamdevhub.devhub.small.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.UserPositionAdapter;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.spring.persistence.user.FakeJpaUserPositionRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.TEST_GUID_1;

public class UserPositionAdapterTest {

    private UserPositionAdapter userPositionAdapter;

    @BeforeEach
    void init() {
        JpaUserPositionRepository jpaUserPositionRepository = new FakeJpaUserPositionRepository();
        IdentifierProvider identifierProvider = new FakeUuidIdentifierProvider(TEST_GUID_1);
        userPositionAdapter = new UserPositionAdapter(jpaUserPositionRepository, identifierProvider);
    }

    @Test
    @DisplayName("전체_관심_포지션을_저장한다")
    void saveAll_savesPositionsCorrectly() {
        // given
        UserPosition pos1 = new UserPosition("user-1", "001");
        UserPosition pos2 = new UserPosition("user-1", "002");
        Set<UserPosition> positions = new HashSet<>(Set.of(pos1, pos2));

        // when
        userPositionAdapter.saveAll(positions);

        // then
        Set<UserPosition> saved = userPositionAdapter.findByUserGuid("user-1");
        assertThat(saved).hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder("001", "002");
    }

    @Test
    @DisplayName("관심포지션이_변경되면_모든_변경사항이_반영된다")
    void replace_mergesOldAndNewPositionsCorrectly() {
        // given
        UserPosition oldPos = new UserPosition("user-1", "001");
        Set<UserPosition> previousPositions = new HashSet<>(Set.of(oldPos));
        userPositionAdapter.saveAll(previousPositions);

        UserPosition newPos = new UserPosition("user-1", "002");
        Set<UserPosition> currentPositions = new HashSet<>(Set.of(oldPos, newPos));

        // when
        userPositionAdapter.replace(previousPositions, currentPositions);

        // then
        Set<UserPosition> finalPositions = userPositionAdapter.findByUserGuid("user-1");
        assertThat(finalPositions).hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder("001", "002");
    }

    @Test
    @DisplayName("변경사항에서_삭제될_값으로_식별된_포지션_값은_삭제된다")
    void replace_removesDeletedPositionsCorrectly() {
        // given
        UserPosition oldPos1 = new UserPosition("user-1", "001");
        UserPosition oldPos2 = new UserPosition("user-1", "002");
        Set<UserPosition> previousPositions = new HashSet<>(Set.of(oldPos1, oldPos2));
        userPositionAdapter.saveAll(previousPositions);

        Set<UserPosition> currentPositions = new HashSet<>(Set.of(oldPos1));

        // when
        userPositionAdapter.replace(previousPositions, currentPositions);

        // then
        Set<UserPosition> finalPositions = userPositionAdapter.findByUserGuid("user-1");
        assertThat(finalPositions).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly("001");
    }

    @Test
    @DisplayName("변경사항이_존재하지_않으면_변경되지_않는다")
    void replace_noChanges_doesNothing() {
        // given
        UserPosition pos = new UserPosition("user-1", "001");
        Set<UserPosition> positions = new HashSet<>(Set.of(pos));
        userPositionAdapter.saveAll(positions);

        // when
        userPositionAdapter.replace(positions, positions); // 같은 값

        // then
        Set<UserPosition> finalPositions = userPositionAdapter.findByUserGuid("user-1");
        assertThat(finalPositions).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly("001");
    }
}