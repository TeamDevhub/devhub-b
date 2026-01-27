package teamdevhub.devhub.infrastructure.user.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserEntity;

public interface UserQueryRepository {

    Page<UserEntity> listUser(SearchUserCommand searchUserCommand, Pageable pageable);
}
