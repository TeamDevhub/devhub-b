package teamdevhub.devhub.adapter.out.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.adapter.out.common.converter.BooleanToYNConverter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "application_form")
public class ApplicationFormEntity {
	
	@Id
	@Column(name = "application_form_guid", length = 32, nullable = false)
	private String applicationFormGuid;

	@Column(name = "type_cd", nullable = false)
	private String typeCd;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "help_text", nullable = false)
	private String helpText;

	@Column(name = "max_length", nullable = false)
	private String maxLength;

	@Column(name = "vert_yn", nullable = false)
	@Convert(converter = BooleanToYNConverter.class)
	private boolean isVertical;

	@Column(name = "custom_yn", nullable = false)
	@Convert(converter = BooleanToYNConverter.class)
	private boolean isCustomized;

	@Column(name = "use_yn", nullable = false)
	@Convert(converter = BooleanToYNConverter.class)
	private boolean isUsed;

	@Column(name = "registrant_guid", nullable = false)
	private String registrantGuid;

	@Column(name = "registered_date", nullable = false)
	private String registeredDate;

	@Column(name = "modifier_guid", nullable = false)
	private String modifierGuid;

	@Column(name = "modified_date", nullable = false)
	private String modifiedDate;
}