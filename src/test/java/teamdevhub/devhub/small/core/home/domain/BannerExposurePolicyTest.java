package teamdevhub.devhub.small.core.home.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.home.domain.Banner;
import teamdevhub.devhub.core.home.domain.policy.BannerExposurePolicy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BannerExposurePolicyTest {

    @Test
    @DisplayName("노출_기간_내의_배너만_필터링된다")
    void filterExposable_withinPeriod_returnsOnlyExposable() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);

        Banner active = Banner.of("b1", "타이틀1", null, null, true, true,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 1);
        Banner expired = Banner.of("b2", "타이틀2", null, null, true, true,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30), 2);
        Banner notStarted = Banner.of("b3", "타이틀3", null, null, true, true,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30), 3);

        // when
        List<Banner> result = BannerExposurePolicy.filterExposable(List.of(active, expired, notStarted), today);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBannerGuid()).isEqualTo("b1");
    }

    @Test
    @DisplayName("useYn이_N인_배너는_노출되지_않는다")
    void filterExposable_notUsed_excluded() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);
        Banner notUsed = Banner.of("b1", "타이틀", null, null, true, false,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 1);

        // when
        List<Banner> result = BannerExposurePolicy.filterExposable(List.of(notUsed), today);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("노출_기간이_null인_배너는_노출되지_않는다")
    void filterExposable_nullDates_excluded() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);
        Banner nullDates = Banner.of("b1", "타이틀", null, null, true, true, null, null, 1);

        // when
        List<Banner> result = BannerExposurePolicy.filterExposable(List.of(nullDates), today);

        // then
        assertThat(result).isEmpty();
    }
}
