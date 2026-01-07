package teamdevhub.devhub.adapter.in.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.adapter.in.common.pagination.PageVo;
import teamdevhub.devhub.common.enums.SuccessCode;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataListResponseVo<T> {

    private boolean success;

    private String code;

    private List<T> dataList;

    private PageVo pagination;

    private ErrorResponseVo error;

    public static <T> ApiDataListResponseVo<T> successWithDataList(SuccessCode successCode, List<T> dataList, PageVo pageVo) {
        return ApiDataListResponseVo.<T>builder()
                .success(true)
                .code(successCode.getCode())
                .dataList(dataList)
                .pagination(pageVo)
                .build();
    }
}