package teamdevhub.devhub.small.core.home.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.home.domain.policy.ProjectExposurePolicy;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectExposurePolicyTest {

    @Test
    @DisplayName("모집_기간_내의_프로젝트는_활성_모집_상태이다")
    void isActiveRecruitment_withinPeriod_returnsTrue() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);
        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 5, 31);

        // when
        boolean result = ProjectExposurePolicy.isActiveRecruitment(start, end, today);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("모집_기간_이후의_프로젝트는_활성_모집_상태가_아니다")
    void isActiveRecruitment_afterPeriod_returnsFalse() {
        // given
        LocalDate today = LocalDate.of(2026, 6, 1);
        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 5, 31);

        // when
        boolean result = ProjectExposurePolicy.isActiveRecruitment(start, end, today);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("모집_날짜가_null이면_활성_모집_상태가_아니다")
    void isActiveRecruitment_nullDates_returnsFalse() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);

        // when
        boolean result = ProjectExposurePolicy.isActiveRecruitment(null, null, today);

        // then
        assertThat(result).isFalse();
    }
}
