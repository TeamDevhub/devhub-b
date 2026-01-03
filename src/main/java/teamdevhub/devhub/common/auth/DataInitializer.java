package teamdevhub.devhub.common.auth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserPositionRepository;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserRepository;
import teamdevhub.devhub.adapter.out.user.persistence.JpaUserSkillRepository;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.common.IdentifierProvider;
import teamdevhub.devhub.port.out.common.PasswordPolicyProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final JpaUserRepository jpaUserRepository;
    private final JpaUserPositionRepository jpaUserPositionRepository;
    private final JpaUserSkillRepository jpaUserSkillRepository;
    private final IdentifierProvider identifierProvider;
    private final PasswordPolicyProvider passwordPolicyProvider;

    @PostConstruct
    public void init() {
        String userGuid = identifierProvider.generateIdentifier();
        String password = passwordPolicyProvider.encode("password123!");

        UserEntity user = UserEntity.builder()
                .userGuid(userGuid)
                .email("test@example.com")
                .password(password)
                .username("tester")
                .userRole(UserRole.USER)
                .introduction("테스트 유저")
                .mannerDegree(36.5)
                .blocked(false)
                .deleted(false)
                .build();

        jpaUserRepository.save(user);

        jpaUserPositionRepository.save(UserPositionEntity.builder()
                .userInterestPositionGuid(identifierProvider.generateIdentifier())
                .userGuid(userGuid)
                .positionCd("001")
                .build());

        jpaUserSkillRepository.save(UserSkillEntity.builder()
                .userSkillGuid(identifierProvider.generateIdentifier())
                .userGuid(userGuid)
                .skillCd("001")
                .build());

        log.info("기본 USER 계정 생성됨 - ID : test@example.com / PW : password123!");
    }
}
