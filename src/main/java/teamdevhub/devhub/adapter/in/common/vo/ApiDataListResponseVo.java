package teamdevhub.devhub.adapter.in.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;

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

    public static <T> ApiDataListResponseVo<T> successWithDataList(SuccessCodeEnum successCodeEnum, List<T> dataList, PageVo pageVo) {
        return ApiDataListResponseVo.<T>builder()
                .success(true)
                .code(successCodeEnum.getCode())
                .dataList(dataList)
                .pagination(pageVo)
                .build();
    }
}