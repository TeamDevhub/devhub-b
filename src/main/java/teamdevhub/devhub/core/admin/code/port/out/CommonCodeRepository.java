package teamdevhub.devhub.core.admin.code.port.out;

import java.util.List;

import teamdevhub.devhub.outbound.admin.code.adapter.entity.CommonCodeEntity;

public interface CommonCodeRepository {
   boolean existsByCodeId(String codeId);
   List<CommonCodeEntity> findBySuperiorCodeId(String superiorCodeId);
   long count();
   void save(CommonCodeEntity entity);
   void saveAll(List<CommonCodeEntity> entities);
}
