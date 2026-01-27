package teamdevhub.devhub.infrastructure.application.adapter.out.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project_application_answer")
public class ProjectApplicationAnswerEntity extends BaseEntity {
	
	@Id
	@Column(name = "application_answer_guid", length = 32, nullable = false)
	private String applicationAnswerGuid;

	@Column(name = "application_guid", length = 32, nullable = false)
	private String applicationGuid;

	@Column(name = "project_application_form_guid", length = 32, nullable = false)
	private String projectApplicationFormGuid;

	@Column(name = "file_guid", length = 32)
	private String fileGuid;

	@Column(name = "content", nullable = false)
	private String content;
}