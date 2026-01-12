package teamdevhub.devhub.adapter.out.user;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserPositionMapper;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.port.out.user.UserPositionRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserPositionAdapter implements UserPositionRepository {

    private final JpaUserPositionRepository jpaUserPositionRepository;
    private final IdentifierProvider identifierProvider;
    private final EntityManager entityManager;

    @Override
    public Set<UserPosition> findByUserGuid(String userGuid) {
        return jpaUserPositionRepository.findByUserGuid(userGuid)
                .stream()
                .map(UserPositionMapper::toRecord)
                .collect(Collectors.toSet());
    }

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
    public void replaceAll(Set<UserPosition> positions) {
        if (positions.isEmpty()) {
            return;
        }

        String userGuid = positions.iterator().next().userGuid();

        jpaUserPositionRepository.deleteByUserGuid(userGuid);
        entityManager.flush();
        entityManager.clear();

        List<UserPositionEntity> userPositionEntityList = positions.stream()
                .map(userPosition -> UserPositionEntity.builder()
                        .userPositionGuid(identifierProvider.generateIdentifier())
                        .userGuid(userPosition.userGuid())
                        .positionCd(userPosition.positionCode())
                        .build())
                .toList();

        jpaUserPositionRepository.saveAll(userPositionEntityList);
    }
}
