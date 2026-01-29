package teamdevhub.devhub.core.project.domain.projectRequirement;

import lombok.Builder;

@Builder
public record ProjectRequirement(String projectRequirementGuid, String position, String level, Integer capacity) {

}
