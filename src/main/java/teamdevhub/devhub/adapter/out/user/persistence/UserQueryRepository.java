package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;

public interface UserQueryRepository {
    Page<UserEntity> listUser(SearchUserCommand searchUserCommand, Pageable pageable);
}
