package teamdevhub.devhub.adapter.out.notification.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

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
import teamdevhub.devhub.adapter.out.common.converter.BooleanToYNConverter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "notification",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_notification_guid",
                        columnNames = "notificationGuid"
                )
        }
)
public class NotificationEntity {
	
	@Id
	@Column(length = 32, nullable = false, unique = true)
    private String notificationGuid;

    @Column(name = "image_guid", length = 32, nullable = false)
    private String imageGuid;
    
    @Column(name="type_cd", length = 10, nullable = false)
    private String typeCd;
    
    @Column(name = "content", nullable = false)
    private String content;
    
    @Column( nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean checked;
    
    @CreatedBy
    @Column(updatable = false)
    private String registrantGuid;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime registeredDate;
}
