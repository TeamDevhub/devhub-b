package teamdevhub.devhub.adapter.in.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "ApiResponse", description = "표준 API 응답 구조")
public class ApiDataListResponseVo<T> {

    @Schema(description = "성공 여부")
    private boolean success;
    @Schema(description = "응답 코드")
    private String code;
    @Schema(description = "다건 응답 데이터")
    private List<T> dataList;
    @Schema(description = "페이징 정보")
    private PageVo pagination;
    @Schema(description = "에러 발생 시 상세 정보")
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