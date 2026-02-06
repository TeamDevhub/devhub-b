package teamdevhub.devhub.core.admin.code.port.out;

import teamdevhub.devhub.core.admin.code.domain.CommonCode;

import java.util.List;

public interface CommonCodeRepository {
    List<CommonCode> getCommonCodeList();
}
