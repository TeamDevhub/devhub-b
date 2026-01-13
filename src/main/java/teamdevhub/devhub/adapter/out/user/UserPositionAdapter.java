package teamdevhub.devhub.adapter.out.user;

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
    public void delete(Set<UserPosition> positions) {
        if (positions == null || positions.isEmpty()) {
            return;
        }

        positions.forEach(position ->
                jpaUserPositionRepository.deleteByUserGuidAndPositionCd(
                        position.userGuid(),
                        position.positionCode()
                )
        );
    }
}
