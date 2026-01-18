package teamdevhub.devhub.port.in.user.usecase;

public interface UserLoginUseCase {

    void updateLastLoginDateTime(String userGuid);
}
