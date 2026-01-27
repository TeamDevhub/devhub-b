package teamdevhub.devhub.adapter.out.trms.entity;

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
        name = "trms"
)
public class TrmsEntity extends BaseEntity {

    @Id @Column(length = 32)
    private String trmsGuid;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "content", nullable = false)
    private String content;
    
    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean requiredYn;
    
    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean useYn;
    
    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean deleteYn;
}