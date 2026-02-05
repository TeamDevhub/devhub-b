package teamdevhub.devhub.core.project.domain.vo.requirement;

import lombok.Builder;

@Builder
public record ProjectRequirement(String projectGuid, String position, String level, Integer capacity) {

}
