package teamdevhub.devhub.adapter.in.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.adapter.in.common.vo.ErrorResponseVo;
import teamdevhub.devhub.adapter.in.common.vo.PageVo;
import teamdevhub.devhub.common.enums.SuccessCode;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataListApiResponseDto<T> {

    private boolean success;
    private String code;
    private List<T> dataList;
    private PageVo pagination;
    private ErrorResponseVo error;

    public static <T> DataListApiResponseDto<T> successWithDataList(SuccessCode successCode, List<T> dataList, PageVo pageVo) {
        return DataListApiResponseDto.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .dataList(dataList)
                .pagination(pageVo)
                .build();
    }
}