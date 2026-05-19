package teamdevhub.devhub.core.home.port.in.query;

import lombok.Builder;

@Builder
public record HomeProjectQuery(int limit) {

    public static HomeProjectQuery of(int limit) {
        return HomeProjectQuery.builder()
                .limit(limit > 0 ? limit : 6)
                .build();
    }
}
