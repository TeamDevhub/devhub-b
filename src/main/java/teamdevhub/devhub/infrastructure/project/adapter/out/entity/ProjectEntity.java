package teamdevhub.devhub.infrastructure.project.adapter.out.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.persistence.jpa.converter.BooleanToYNConverter;
import teamdevhub.devhub.shared.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "project",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_project_guid",
                        columnNames = "projectGuid"
                )
        }
)
public class ProjectEntity extends BaseEntity {
	
	@Id
	@Column(length = 32, nullable = false, unique = true)
    private String projectGuid;

    @Column(name = "file_guid", length = 32)
    private String fileGuid;

    @Column( name = "user_guid", length = 32, nullable = false)
    private String userGuid;
    
    @Column(name = "recuritment_type_cd", length = 10, nullable = false)
    private String recuritmentTypeCd;
    
    @Column(name = "progress_type_cd", length = 10, nullable = false)
    private String progressTypeCd;
    
    @Column(name = "nickname", nullable = false)
    private String nickname;
    
    @Column(name = "category", nullable = false)
    private String category;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "content", nullable = false)
    private String content;
    
    @Column(name = "recuritment_start_date", nullable = false)
    private LocalDateTime recuritmentStartDate;
    
    @Column(name = "recuritment_end_date", nullable = false)
    private LocalDateTime recuritmentEndDate;
    
    @Column(name = "progress_start_date", nullable = false)
    private LocalDateTime progressStartDate;
    
    @Column(name = "progress_end_date", nullable = false)
    private LocalDateTime progressEndDate;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false)
    private boolean deleted;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column( nullable = false)
    private boolean capacityClosed;
}
