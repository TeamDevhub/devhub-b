package teamdevhub.devhub.adapter.out.code.entity;

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
@Table(name = "common_code")
public class CommonCodeEntity {
	
	@Id
    @Column(name = "code_id", nullable = false)	
	private String codeId;
	
	@Column(name = "superior_code_id", nullable = false)	
	private String superiorCodeId;
	
    @Column(name = "name", nullable = false)	
	private String name;
	
    @Column(name = "sort_order", nullable = false)	
	private String sortOrder;
	
    @Column(name = "use_yn", nullable = false)	
	@Convert(converter = BooleanToYNConverter.class)
	private String isUsed;
	
    @Column(name = "remarks")	
	private String remarks;
	
    @Column(name = "registrant_guid", nullable = false)	
	private String registrantGuid;
	
    @Column(name = "registered_date", nullable = false)	
	private String registeredDate;
	
    @Column(name = "modifier_guid", nullable = false)	
	private String modifierGuid;
	
    @Column(name = "modified_date", nullable = false)	
	private String modifiedDate;
}