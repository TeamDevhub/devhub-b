package teamdevhub.devhub.infrastructure.user.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserPositionEntity;
import teamdevhub.devhub.infrastructure.user.adapter.out.mapper.UserPositionMapper;
import teamdevhub.devhub.infrastructure.user.adapter.out.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.core.user.domain.vo.position.UserPosition;
import teamdevhub.devhub.core.user.port.out.UserPositionRepository;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.shared.util.RelationChangeUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPositionAdapter implements UserPositionRepository {

    private final JpaUserPositionRepository jpaUserPositionRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public void saveAll(Set<UserPosition> positions) {
        if (positions.isEmpty()) {
            return;
        }

        List<UserPositionEntity> userPositionEntityList = positions.stream()
                .map(userPosition -> {
                    String userPositionGuid = identifierProvider.generateIdentifier();
                    return UserPositionMapper.toEntity(userPositionGuid, userPosition);
                })
                .toList();

        jpaUserPositionRepository.saveAll(userPositionEntityList);
    }

    @Override
    public Set<UserPosition> findByUserGuid(String userGuid) {
        return jpaUserPositionRepository.findByUserGuid(userGuid)
                .stream()
                .map(UserPositionMapper::toRecord)
                .collect(Collectors.toSet());
    }

    @Override
    public void replace(Set<UserPosition> previousPositions, Set<UserPosition> currentPositions) {
        if (currentPositions == null || currentPositions.isEmpty()) {
            return;
        }

        syncPositions(previousPositions, currentPositions);
    }

    private void syncPositions(Set<UserPosition> previousPositions, Set<UserPosition> currentPositions) {
        String userGuid = currentPositions.iterator().next().userGuid();

        Set<String> oldPositionCds = previousPositions.stream()
                .map(UserPosition::positionCd)
                .collect(Collectors.toSet());

        Set<String> newPositionCds = currentPositions.stream()
                .map(UserPosition::positionCd)
                .collect(Collectors.toSet());

        RelationChangeUtil.RelationChange<String> change = RelationChangeUtil.change(oldPositionCds, newPositionCds);

        if (change.isEmpty()) {
            return;
        }

        deletePositions(userGuid, change.toDelete());
        insertPositions(userGuid, change.toInsert());
    }

    private void deletePositions(String userGuid, Set<String> positionCds) {
        if (!positionCds.isEmpty()) {
            jpaUserPositionRepository.deleteByUserGuidAndPositionCdIn(userGuid, positionCds);
        }
    }

    private void insertPositions(String userGuid, Set<String> positionCds) {
        if (positionCds.isEmpty()) {
            return;
        }

        List<UserPositionEntity> entities = positionCds.stream()
                .map(positionCd -> UserPositionEntity.builder()
                        .userPositionGuid(identifierProvider.generateIdentifier())
                        .userGuid(userGuid)
                        .positionCd(positionCd)
                        .build())
                .toList();

        jpaUserPositionRepository.saveAll(entities);
    }
}
