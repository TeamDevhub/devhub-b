package teamdevhub.devhub.adapter.out.banner.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(
        name = "banner",
        uniqueConstraints = {
        		@UniqueConstraint(
                        name = "uk_banner_guid",
                        columnNames = "bannerGuid"
                )
        }
)
public class BannerEntity extends BaseEntity {
	
	@Id
	@Column(length = 32, nullable = false, unique = true)
    private String bannerGuid;

    @Column(name = "image_guid", length = 32, nullable = false)
    private String imageGuid;
    
    @Column( nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean used;
    
    @Column( nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean idMainBanner;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "publication_start_date", nullable = false)
    private LocalDateTime publicationStartDate;
    
    @Column(name = "publication_end_date", nullable = false)
    private LocalDateTime publicationEndDate;
}
