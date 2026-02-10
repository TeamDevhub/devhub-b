package teamdevhub.devhub.core.application.domain;

import lombok.Builder;

@Builder
public record ProjectApplicationForm(String projectApplicationFormGuid, String projectGuid, String applicationFormGuid) {

}
