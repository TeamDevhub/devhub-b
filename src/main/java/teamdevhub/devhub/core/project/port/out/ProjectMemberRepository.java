package teamdevhub.devhub.core.project.port.out;

public interface ProjectMemberRepository {

    boolean isMember(String projectGuid, String userGuid);
}
