package teamdevhub.devhub.adapter.out.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project_application_answer")
public class ProjectApplicationAnswerEntity {
	
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

	@Column(name = "registrant_guid", nullable = false)
	private String registrantGuid;

	@Column(name = "registered_date", nullable = false)
	private String registeredDate;

	@Column(name = "modifier_guid", nullable = false)
	private String modifierGuid;

	@Column(name = "modified_date", nullable = false)
	private String modifiedDate;
}