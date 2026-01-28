package teamdevhub.devhub.medium.outbound.user.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import teamdevhub.devhub.outbound.user.adapter.UserPositionAdapter;
import teamdevhub.devhub.outbound.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest
@Transactional
class UserPositionAdapterMediumTest {

    @Autowired
    private UserPositionAdapter userPositionAdapter;

    @Autowired
    private JpaUserPositionRepository jpaUserPositionRepository;

    @BeforeEach
    void init() {
        jpaUserPositionRepository.deleteAll();
    }

    @Test
    @DisplayName("전체_관심_포지션을_저장한다")
    void saveAll_savesPositionsCorrectly() {
        // given
        UserPosition pos1 = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        UserPosition pos2 = new UserPosition(TEST_USER_GUID_1, NEW_POSITION_CD);

        Set<UserPosition> positions = new HashSet<>(Set.of(pos1, pos2));

        // when
        userPositionAdapter.saveAll(positions);

        // then
        Set<UserPosition> saved = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(saved).hasSize(2)
                .extracting(UserPosition::positionCd)
                .containsExactlyInAnyOrder(TEST_POSITION_CD, NEW_POSITION_CD);
    }

    @Test
    @DisplayName("saveAll_은_빈_Set_이면_아무것도_저장하지_않는다")
    void saveAll_emptySet_doesNothing() {
        // given
        Set<UserPosition> emptyPositions = new HashSet<>();

        // when
        userPositionAdapter.saveAll(emptyPositions);

        // then
        Set<UserPosition> saved = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(saved).isEmpty();
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

        Set<UserPosition> previousPositions =
                new HashSet<>(Set.of(oldPos1, oldPos2));

        userPositionAdapter.saveAll(previousPositions);

        Set<UserPosition> currentPositions =
                new HashSet<>(Set.of(oldPos1));

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
        UserPosition pos = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> positions = new HashSet<>(Set.of(pos));
        userPositionAdapter.saveAll(positions);

        // when
        userPositionAdapter.replace(positions, positions);

        // then
        Set<UserPosition> finalPositions =
                userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);

        assertThat(finalPositions).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);
    }

    @Test
    @DisplayName("replace_는_null_또는_빈_셋이면_아무것도_수정하지_않는다")
    void replace_nullOrEmpty_doesNothing() {
        // given
        UserPosition pos = new UserPosition(TEST_USER_GUID_1, TEST_POSITION_CD);
        Set<UserPosition> previousPositions = new HashSet<>(Set.of(pos));
        userPositionAdapter.saveAll(previousPositions);

        // when
        userPositionAdapter.replace(previousPositions, null);
        // then
        Set<UserPosition> finalPositions1 = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(finalPositions1).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);

        // when
        userPositionAdapter.replace(previousPositions, new HashSet<>());

        // then
        Set<UserPosition> finalPositions2 = userPositionAdapter.findByUserGuid(TEST_USER_GUID_1);
        assertThat(finalPositions2).hasSize(1)
                .extracting(UserPosition::positionCd)
                .containsExactly(TEST_POSITION_CD);
    }
}