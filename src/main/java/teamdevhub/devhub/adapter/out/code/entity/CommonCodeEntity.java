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
import teamdevhub.devhub.adapter.out.common.entity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "common_code")
public class CommonCodeEntity extends BaseEntity {
	
	@Id
    @Column(name = "code_id", nullable = false)	
	private String codeId;
	
	@Column(name = "superior_code_id", nullable = false)	
	private String superiorCodeId;
	
    @Column(name = "name", nullable = false)	
	private String name;
	
    @Column(name = "sort_order", nullable = false)	
	private String sortOrder;
	
	@Convert(converter = BooleanToYNConverter.class)
    @Column(name = "use_yn", nullable = false)
	private String isUsed;
	
    @Column(name = "remarks")	
	private String remarks;
}