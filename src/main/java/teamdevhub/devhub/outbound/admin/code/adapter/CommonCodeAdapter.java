package teamdevhub.devhub.outbound.admin.code.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.core.admin.code.port.out.CommonCodeRepository;
import teamdevhub.devhub.outbound.admin.code.adapter.entity.CommonCodeEntity;
import teamdevhub.devhub.outbound.admin.code.adapter.mapper.CommonCodeMapper;
import teamdevhub.devhub.outbound.admin.code.persistence.CommonCodeQueryRepository;
import teamdevhub.devhub.outbound.admin.code.persistence.JpaCommonCodeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommonCodeAdapter implements CommonCodeRepository {

    private final CommonCodeQueryRepository commonCodeQueryRepository;
    private final JpaCommonCodeRepository jpaCommonCodeRepository;

    @Override
    public List<CommonCode> getCommonCodeList() {
        List<CommonCodeEntity> codeList = jpaCommonCodeRepository.findAllByOrderBySortOrderAsc();
        return CommonCodeMapper.convertToTree(codeList);
    }
}
