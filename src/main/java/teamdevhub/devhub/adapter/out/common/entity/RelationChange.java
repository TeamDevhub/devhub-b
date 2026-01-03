package teamdevhub.devhub.adapter.out.common.entity;

import java.util.Set;

public record RelationChange<T>(
        Set<T> toInsert,
        Set<T> toDelete
) {
    public boolean isEmpty() {
        return toInsert.isEmpty() && toDelete.isEmpty();
    }

    public static <T> RelationChange<T> of(Set<T> toInsert, Set<T> toDelete) {
        return new RelationChange<>(Set.copyOf(toInsert), Set.copyOf(toDelete));
    }
}
