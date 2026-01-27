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
        name = "terms_agreement"
)
public class TermsAgreementEntity extends BaseEntity {

    @Id @Column(length = 32)
    private String trmsAgreementGuid;
    
    @Column(name = "trms_guid", nullable = false)
    private String trmsGuid;
    
    @Column(name = "user_guid", nullable = false)
    private String userGuid;
    
    @Column(name = "agreement_yn", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean isAgreed;
}