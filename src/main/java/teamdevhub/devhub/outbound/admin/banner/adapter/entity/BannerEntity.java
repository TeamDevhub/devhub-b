package teamdevhub.devhub.outbound.admin.banner.adapter.entity;

import jakarta.persistence.*;
import lombok.*;
import teamdevhub.devhub.outbound.common.persistence.jpa.audit.BaseEntity;
import teamdevhub.devhub.outbound.common.persistence.jpa.converter.NullableBooleanToYNConverter;

import java.time.LocalDateTime;

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

    @Column(name = "image_guid", length = 32)
    private String imageGuid;
    
    @Convert(converter = NullableBooleanToYNConverter.class)
    @Column(nullable = false)
    private boolean used;
    
    @Convert(converter = NullableBooleanToYNConverter.class)
    @Column(nullable = false)
    private boolean isMainBanner;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;


    @Column(name = "publication_start_date")
    private LocalDateTime publicationStartDate;
    
    @Column(name = "publication_end_date")
    private LocalDateTime publicationEndDate;
}
