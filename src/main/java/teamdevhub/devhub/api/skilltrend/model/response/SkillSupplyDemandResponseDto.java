package teamdevhub.devhub.api.skilltrend.model.response;

import teamdevhub.devhub.core.skilltrend.domain.vo.SkillSupplyDemand;

public record SkillSupplyDemandResponseDto(
        String skillCd,
        long demandCnt,
        long supplyCnt
) {

    public static SkillSupplyDemandResponseDto from(SkillSupplyDemand skillSupplyDemand) {
        return new SkillSupplyDemandResponseDto(
                skillSupplyDemand.skillCd(),
                skillSupplyDemand.demandCnt(),
                skillSupplyDemand.supplyCnt()
        );
    }
}
