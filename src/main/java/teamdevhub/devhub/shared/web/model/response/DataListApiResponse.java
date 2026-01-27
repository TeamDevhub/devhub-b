package teamdevhub.devhub.shared.web.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataListApiResponse<T> {

    @JsonProperty("isSuccess")
    private boolean isSuccess;
    private String code;
    private List<T> dataList;
    private PageResponse pagination;
    private ErrorResponse error;

    public static <T> DataListApiResponse<T> successWithDataList(SuccessCode successCode, List<T> dataList, PageResponse pageResponse) {
        return DataListApiResponse.<T>builder()
                .isSuccess(true)
                .code(successCode.getCode())
                .dataList(dataList)
                .pagination(pageResponse)
                .build();
    }
}