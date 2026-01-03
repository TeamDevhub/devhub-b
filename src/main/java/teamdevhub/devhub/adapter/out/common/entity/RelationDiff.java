package teamdevhub.devhub.adapter.out.common.entity;

import java.util.Set;

public record RelationDiff<T>(
        Set<T> toInsert,
        Set<T> toDelete
) {
    public boolean isEmpty() {
        return toInsert.isEmpty() && toDelete.isEmpty();
    }
}
