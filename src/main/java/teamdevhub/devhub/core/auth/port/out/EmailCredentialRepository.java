package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.vo.EmailCredential;

public interface EmailCredentialRepository {

    EmailCredential findByEmail(String email);
    EmailCredential findByUserGuid(String userGuid);
}
