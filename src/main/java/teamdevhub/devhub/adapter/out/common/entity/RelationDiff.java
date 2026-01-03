package teamdevhub.devhub.adapter.out.common.entity;

import java.util.Set;

public record RelationDiff<T>(
        Set<T> toInsert,
        Set<T> toDelete
) {
    public boolean isEmpty() {
        return toInsert.isEmpty() && toDelete.isEmpty();
    }

    public static <T> RelationDiff<T> of(Set<T> toInsert, Set<T> toDelete) {
        return new RelationDiff<>(Set.copyOf(toInsert), Set.copyOf(toDelete));
    }
}
