package teamdevhub.devhub.core.admin.code.port.out;

import teamdevhub.devhub.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.core.admin.code.domain.CommonCodeDetail;

import java.util.List;

public interface CommonCodeRepository {
    List<CommonCode> getCommonCodeList();
    List<CommonCodeDetail> getCommonCodeDetailList();
    void saveAll(List<CommonCode> commonCodes);
    void save(CommonCode commonCode);
}
