package teamdevhub.devhub.core.auth.port.in.usecase;

public interface LoginPolicyUseCase {

    void updateLastLoginDateTime(String userGuid);
    void validateLoginUser(String userGuid);
}
