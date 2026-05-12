package teamdevhub.devhub.core.report.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.board.domain.Board;
import teamdevhub.devhub.core.board.domain.Comment;
import teamdevhub.devhub.core.board.port.out.BoardRepository;
import teamdevhub.devhub.core.board.port.out.CommentRepository;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.report.domain.Report;
import teamdevhub.devhub.core.report.port.in.command.CreateReportCommand;
import teamdevhub.devhub.core.report.port.in.usecase.ReportUseCase;
import teamdevhub.devhub.core.report.port.out.ReportRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService implements ReportUseCase {

    private final IdentifierProvider identifierProvider;
    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Override
    public void createReport(CreateReportCommand createReportCommand) {
        String boardGuid = createReportCommand.boardGuid();

        boolean duplicated = reportRepository.existsDuplicate(createReportCommand.reporterUser(), boardGuid, createReportCommand.commentGuid());
        if (duplicated) {
            throw BusinessRuleException.of(ErrorCode.REPORT_DUPLICATE);
        }

        // 신고당한 회원이 실제 작성자인지 확인
        if (boardGuid != null && !boardGuid.isBlank()) {
            Board board = boardRepository.findByBoardGuid(boardGuid);
            if (!board.getUserGuid().equals(createReportCommand.reportedUser())) {
                throw BusinessRuleException.of(ErrorCode.REPORT_TARGET_MISMATCH);
            }
        } else {
            Comment comment = commentRepository.findByCommentGuid(createReportCommand.commentGuid());
            if (!comment.getUserGuid().equals(createReportCommand.reportedUser())) {
                throw BusinessRuleException.of(ErrorCode.REPORT_TARGET_MISMATCH);
            }
        }

        String reportGuid = identifierProvider.generateIdentifier();
        Report report = Report.createReport(createReportCommand, reportGuid);
        reportRepository.save(report);
    }
}
