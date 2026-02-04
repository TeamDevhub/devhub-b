package teamdevhub.devhub.outbound.admin.code.adapter.mapper;

import teamdevhub.devhub.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.outbound.admin.code.adapter.entity.CommonCodeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonCodeMapper {

    private static AuditInfo toAuditInfo(CommonCodeEntity entity) {
        return AuditInfo.of(
                entity.getRegistrantGuid(),
                entity.getRegisteredDate(),
                entity.getModifierGuid(),
                entity.getModifiedDate()
        );
    }

    public static CommonCode toDomain(CommonCodeEntity entity) {
        if (entity == null) return null;
        return CommonCode.builder()
                .isUsed(entity.isUsed())
                .parentCode(entity.getSuperiorCodeId())
                .remarks(entity.getRemarks())
                .code(entity.getCodeId())
                .name(entity.getName())
                .order(entity.getSortOrder())
                .auditInfo(toAuditInfo(entity))
                .build();
    }

    public static List<CommonCode> convertToTree(List<CommonCodeEntity> allCodes) {
        Map<String, CommonCode> map = allCodes.stream()
                .map(CommonCodeMapper::toDomain)
                .collect(Collectors.toMap(CommonCode::getCode, domain -> domain));

        List<CommonCode> result = new ArrayList<>();
        map.values().forEach(domain -> {
            String parentCode = domain.getParentCode();
            if ("0000".equals(parentCode) || parentCode == null || parentCode.isEmpty()) {
                result.add(domain);
            } else {
                CommonCode parent = map.get(parentCode);
                if (parent != null) {
                    parent.getChildren().add(domain);
                }
            }
        });

        return result;
    }
}
