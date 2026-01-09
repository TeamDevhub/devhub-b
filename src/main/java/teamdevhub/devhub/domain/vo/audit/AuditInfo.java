package teamdevhub.devhub.domain.vo.audit;

import java.time.LocalDateTime;

public record AuditInfo(String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {

    private static final AuditInfo EMPTY = new AuditInfo(null, null, null, null);

    public static AuditInfo of(String createdBy,
                               LocalDateTime createdAt,
                               String modifiedBy,
                               LocalDateTime modifiedAt)
    {
        return new AuditInfo(createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static AuditInfo empty() {
        return EMPTY;
    }
}
