package teamdevhub.devhub.outbound.admin.code.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.core.admin.code.domain.CommonCodeDetail;
import teamdevhub.devhub.core.admin.code.port.out.CommonCodeRepository;
import teamdevhub.devhub.outbound.admin.code.adapter.entity.CommonCodeEntity;
import teamdevhub.devhub.outbound.admin.code.adapter.mapper.CommonCodeMapper;
import teamdevhub.devhub.outbound.admin.code.persistence.JpaCommonCodeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommonCodeAdapter implements CommonCodeRepository {

    private final JpaCommonCodeRepository jpaCommonCodeRepository;

    @Override
    public List<CommonCodeDetail> getCommonCodeDetailList() {
        List<CommonCodeEntity> codeList = jpaCommonCodeRepository.findAllByOrderBySortOrderAsc();
        return CommonCodeMapper.convertToTree(codeList);
    }

    @Override
    public List<CommonCode> getCommonCodeList() {
        List<CommonCodeEntity> codeList = jpaCommonCodeRepository.findBySuperiorCodeIdOrderBySortOrderAsc("0000");
        return codeList.stream()
                .map(CommonCodeMapper::toDomain)
                .toList();
    }

    @Override
    public void saveAll(List<CommonCode> commonCodes) {
        jpaCommonCodeRepository.saveAll(commonCodes.stream().map(CommonCodeMapper::toEntity).toList());
    }

    @Override
    public void save(CommonCode commonCode) {
        jpaCommonCodeRepository.save(CommonCodeMapper.toEntity(commonCode));
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaCommonCodeRepository.existsById(code);
    }
}
