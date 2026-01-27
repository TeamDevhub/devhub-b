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
        name = "trms_agreement"
)
public class TrmsAgreementEntity extends BaseEntity {

    @Id @Column(length = 32)
    private String trmsAgreementGuid;
    
    @Column(name = "trms_guid", nullable = false)
    private String trmsGuid;
    
    @Column(name = "user_guid", nullable = false)
    private String userGuid;
    
    @Column(nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean agreementYn;
}