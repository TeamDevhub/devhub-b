package teamdevhub.devhub.api.admin.banner.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.admin.banner.port.in.command.SearchBannerRequestCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchBannerRequstDto {
    private String publicationStartDate;
    private String publicationEndDate;
    private String alwaysPublication;
    private String used;
    private String keyword;
    private String bannerType;

    public SearchBannerRequestCommand toCommand() {
        return SearchBannerRequestCommand.builder()
                .publicationStartDate(publicationStartDate)
                .publicationEndDate(publicationEndDate)
                .isUsed("Y".equalsIgnoreCase(used) ? Boolean.TRUE : "N".equalsIgnoreCase(used) ? false : null)
                .alwaysPublication("Y".equalsIgnoreCase(alwaysPublication) ? Boolean.TRUE : "N".equalsIgnoreCase(alwaysPublication) ? false : null)
                .keyword(keyword)
                .isMainBanner("MAIN".equalsIgnoreCase(bannerType))
                .build();
    }
}
