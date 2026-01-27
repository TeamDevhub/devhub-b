package teamdevhub.devhub.adapter.out.board.entity;

import jakarta.persistence.*;
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
        name = "report"
)
public class ReportEntity extends BaseEntity {

    @Id @Column(length = 32)
    private String reportGuid;
    
    @Column(name = "board_guid", nullable = false)
    private String boardGuid;
    
    @Column(name = "comment_guid", nullable = false)
    private String commentGuid;
    
    @Column(name = "reported_user", nullable = false)
    private String reportedUser;
    
    @Column(name = "reporter_user", nullable = false)
    private String reporterUser;
    
    @Column(name = "category_cd", nullable = false)
    private String categoryCd;
    
    @Column(name = "reason", nullable = false)
    private String reason;
    
    @Column(name = "process_yn", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean isProcessed;
}