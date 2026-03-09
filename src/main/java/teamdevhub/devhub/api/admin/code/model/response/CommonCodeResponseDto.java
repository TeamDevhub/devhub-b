package teamdevhub.devhub.api.admin.code.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.admin.code.domain.CommonCode;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonCodeResponseDto {

    private String code;
    private String parentCode;
    private String name;
    private boolean isUsed;

    public static CommonCodeResponseDto fromDomain(CommonCode commonCode) {
        return builder()
                .code(commonCode.getCode())
                .parentCode(commonCode.getParentCode())
                .name(commonCode.getName())
                .isUsed(commonCode.isUsed())
                .build();
    }

}
