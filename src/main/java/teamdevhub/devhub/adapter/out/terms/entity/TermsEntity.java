package teamdevhub.devhub.adapter.out.terms.entity;

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
        name = "terms"
)
public class TermsEntity extends BaseEntity {

    @Id @Column(length = 32)
    private String trmsGuid;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "content", nullable = false)
    private String content;
    
    @Column(name = "required_yn", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean isRequired;
    
    @Column(name = "use_yn", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean isUsed;
    
    @Column(name = "delete_yn", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean isDeleted;
}