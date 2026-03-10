package teamdevhub.devhub.core.admin.code.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.admin.code.port.in.command.CommonCodeCommand;
import teamdevhub.devhub.core.common.audit.AuditInfo;

@Getter
@Builder
public class CommonCode {

    private String code;
    private String parentCode;
    private String name;
    private String order;
    private boolean isUsed;
    private String remarks;
    private AuditInfo auditInfo;

    public static CommonCode createCommonCode(CommonCodeCommand commonCodeCommand) {
        return builder()
                .code(commonCodeCommand.code())
                .parentCode(commonCodeCommand.parentCode())
                .name(commonCodeCommand.name())
                .order(commonCodeCommand.order())
                .isUsed(commonCodeCommand.isUsed())
                .remarks(commonCodeCommand.remarks())
                .auditInfo(AuditInfo.empty())
                .build();
    }

}
