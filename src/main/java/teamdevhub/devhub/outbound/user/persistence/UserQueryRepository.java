package teamdevhub.devhub.outbound.user.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;

public interface UserQueryRepository {

    Page<UserEntity> listUser(SearchUserCommand searchUserCommand, Pageable pageable);
}
