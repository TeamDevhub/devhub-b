package teamdevhub.devhub.core.admin.code.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.core.admin.code.domain.CommonCodeDetail;
import teamdevhub.devhub.core.admin.code.port.in.usecase.CommonCodeUseCase;
import teamdevhub.devhub.core.admin.code.port.out.CommonCodeRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonCodeService implements CommonCodeUseCase {

    private final CommonCodeRepository commonCodeRepository;

    @Override
    public List<CommonCodeDetail> getCommonCodeDetailList() {
        return commonCodeRepository.getCommonCodeDetailList();
    }

    @Override
    public List<CommonCode> getCommonCodeList() {
        return commonCodeRepository.getCommonCodeList();
    }

    @Override
    public void saveCommonCode(CommonCode commonCode) {
        commonCodeRepository.save(commonCode);
    }

    @Override
    public void saveCommonCodeList(List<CommonCode> commonCodeList) {
        commonCodeRepository.saveAll(commonCodeList);
    }
}
