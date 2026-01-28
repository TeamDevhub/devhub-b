package teamdevhub.devhub.outbound.file.adapter.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "attachment_file",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_file_guid",
                        columnNames = "fileGuid"
                )
        }
)
public class AttachmentFileEntity extends BaseEntity {
	
	@Id
	@Column(length = 32, nullable = false, unique = true)
    private String fileGuid;

    @Column(name = "original_name", nullable = false)
    private String originalName;
    
    @Column(name = "extension_name", nullable = false)
    private String extensionName;
    
    @Column(name = "size", nullable = false)
    private int size;
    
    @Column(name = "path", nullable = false)
    private String path;
    
}
