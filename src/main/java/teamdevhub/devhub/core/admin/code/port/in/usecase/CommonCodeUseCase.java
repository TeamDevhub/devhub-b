package teamdevhub.devhub.core.admin.code.port.in.usecase;

import teamdevhub.devhub.core.admin.code.domain.CommonCode;

import java.util.List;

public interface CommonCodeUseCase {
    List<CommonCode> getCommonCodeList();
}
