package teamdevhub.devhub.shared.initialize;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.code.port.out.CommonCodeRepository;
import teamdevhub.devhub.outbound.admin.code.adapter.entity.CommonCodeEntity;

@Component
@RequiredArgsConstructor
@Transactional
public class CommonCodeInitialize implements CommandLineRunner {

    private final CommonCodeRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return; // 이미 데이터 있으면 skip
        }

        insertSkillCodes();
        insertPositionCodes();
        insertPositionLevelCodes();
        insertProjectRecruitTypeCodes();
        insertProjectProgressTypeCodes();
        insertProjectRecruitStatusCodes();
        insertProjectApprovalStatusCodes();
        insertPostCategoryCodes();
        insertReportTypeCodes();
        insertNotificationTypeCodes();
        insertRegionCodes();
    }

    /* ===================== SKILL ===================== */
    private void insertSkillCodes() {
        saveAll("SKILL_CODE", List.of(
            code("1001", "SKILL_CODE", "Java", "1"),
            code("1002", "SKILL_CODE", "Spring", "1"),
            code("1003", "SKILL_CODE", "React", "1"),
            code("1004", "SKILL_CODE", "Node.js", "1")
        ));
    }

    /* ===================== POSITION ===================== */
    private void insertPositionCodes() {
        saveAll("POSITION_CODE", List.of(
            code("2001", "POSITION_CODE", "Backend", "1"),
            code("2002", "POSITION_CODE", "Frontend", "1"),
            code("2003", "POSITION_CODE", "Fullstack", "1"),
            code("2004", "POSITION_CODE", "Mobile", "1")
        ));
    }

    private void insertPositionLevelCodes() {
        saveAll("POSITION_LEVEL_CODE", List.of(
            code("2101", "POSITION_LEVEL_CODE", "초급", "1"),
            code("2102", "POSITION_LEVEL_CODE", "중급", "1"),
            code("2103", "POSITION_LEVEL_CODE", "고급", "1")
        ));
    }

    /* ===================== PROJECT ===================== */
    private void insertProjectRecruitTypeCodes() {
        saveAll("PROJECT_RECRUIT_TYPE", List.of(
            code("3001", "PROJECT_RECRUIT_TYPE", "일반모집", "1"),
            code("3002", "PROJECT_RECRUIT_TYPE", "추가모집", "1")
        ));
    }

    private void insertProjectProgressTypeCodes() {
        saveAll("PROJECT_PROGRESS_TYPE", List.of(
            code("3101", "PROJECT_PROGRESS_TYPE", "온라인", "1"),
            code("3102", "PROJECT_PROGRESS_TYPE", "오프라인", "1"),
            code("3103", "PROJECT_PROGRESS_TYPE", "온/오프라인 병행", "1")
        ));
    }

    private void insertProjectRecruitStatusCodes() {
        saveAll("PROJECT_RECRUIT_STATUS", List.of(
            code("3201", "PROJECT_RECRUIT_STATUS", "모집중", "1"),
            code("3202", "PROJECT_RECRUIT_STATUS", "모집완료", "1"),
            code("3203", "PROJECT_RECRUIT_STATUS", "모집대기", "1"),
            code("3204", "PROJECT_RECRUIT_STATUS", "진행중", "1")
        ));
    }

    private void insertProjectApprovalStatusCodes() {
        saveAll("PROJECT_APPROVAL_STATUS", List.of(
            code("3301", "PROJECT_APPROVAL_STATUS", "승인 대기", "1"),
            code("3302", "PROJECT_APPROVAL_STATUS", "승인 완료", "1"),
            code("3303", "PROJECT_APPROVAL_STATUS", "승인 거절", "1")
        ));
    }

    /* ===================== ETC ===================== */
    private void insertPostCategoryCodes() {
        saveAll("POST_CATEGORY", List.of(
            code("4001", "POST_CATEGORY", "자유게시판", "1"),
            code("4002", "POST_CATEGORY", "질문", "1"),
            code("4003", "POST_CATEGORY", "공지사항", "1")
        ));
    }

    private void insertReportTypeCodes() {
        saveAll("REPORT_TYPE", List.of(
            code("5001", "REPORT_TYPE", "스팸/광고", "1"),
            code("5002", "REPORT_TYPE", "욕설/비방", "1"),
            code("5003", "REPORT_TYPE", "불법 콘텐츠", "1")
        ));
    }

    private void insertNotificationTypeCodes() {
        saveAll("NOTIFICATION_TYPE", List.of(
            code("6001", "NOTIFICATION_TYPE", "프로젝트 지원 알림", "1"),
            code("6002", "NOTIFICATION_TYPE", "프로젝트 승인 알림", "1"),
            code("6003", "NOTIFICATION_TYPE", "댓글 알림", "1")
        ));
    }

    /* ===================== REGION ===================== */
    private void insertRegionCodes() {
        // 서울
        saveAll("REGION_CODE", List.of(
            code("8100", "REGION_CODE", "서울", "1"),
            code("810001", "8100", "강남구", "2"),
            code("810002", "8100", "강동구", "2"),
            code("810003", "8100", "강북구", "2"),
            code("810004", "8100", "강서구", "2")
        ));

        // 경기
        saveAll("REGION_CODE", List.of(
            code("8200", "REGION_CODE", "경기", "1"),
            code("820001", "8200", "수원시", "2"),
            code("820002", "8200", "성남시", "2"),
            code("820003", "8200", "고양시", "2"),
            code("820004", "8200", "용인시", "2")
        ));

        // 인천
        saveAll("REGION_CODE", List.of(
            code("8300", "REGION_CODE", "인천", "1"),
            code("830001", "8300", "중구", "2"),
            code("830002", "8300", "동구", "2")
        ));

        // 부산
        saveAll("REGION_CODE", List.of(
            code("8400", "REGION_CODE", "부산", "1"),
            code("840001", "8400", "해운대구", "2"),
            code("840002", "8400", "수영구", "2")
        ));
    }

    /* ===================== 공통 유틸 ===================== */
    private void saveAll(String group, List<CommonCodeEntity> codes) {
        repository.saveAll(codes);
    }

    private CommonCodeEntity code(String codeId, String parentCodeId, String name, String depth) {
        return CommonCodeEntity.builder()
            .codeId(codeId)
            .superiorCodeId(parentCodeId)
            .name(name)
            .sortOrder(depth)
            .isUsed("Y")
            .remarks(null)
            .build();
    }
}
