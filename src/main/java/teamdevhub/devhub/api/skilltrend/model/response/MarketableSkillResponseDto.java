package teamdevhub.devhub.api.skilltrend.model.response;

import teamdevhub.devhub.core.skilltrend.domain.vo.MarketableSkill;

public record MarketableSkillResponseDto(
        String positionCd,
        String skillCd,
        long count
) {

    public static MarketableSkillResponseDto from(MarketableSkill marketableSkill) {
        return new MarketableSkillResponseDto(
                marketableSkill.positionCd(),
                marketableSkill.skillCd(),
                marketableSkill.count()
        );
    }
}
