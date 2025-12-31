package teamdevhub.devhub.domain.common;

import java.time.LocalDateTime;

public class AuditInfo {

    private static final AuditInfo EMPTY = new AuditInfo(null, null, null, null);

    private final String createdBy;
    private final LocalDateTime createdAt;
    private final String modifiedBy;
    private final LocalDateTime modifiedAt;

    public AuditInfo(String createdBy,
                     LocalDateTime createdAt,
                     String modifiedBy,
                     LocalDateTime modifiedAt) {
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
    }

    public static AuditInfo of(String createdBy, LocalDateTime createdAt,
                               String modifiedBy, LocalDateTime modifiedAt) {
        return new AuditInfo(createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static AuditInfo empty() {
        return EMPTY;
    }

    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getModifiedBy() { return modifiedBy; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
}
