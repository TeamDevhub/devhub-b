package teamdevhub.devhub.adapter.out.common.util;

import teamdevhub.devhub.adapter.out.common.entity.RelationChange;

import java.util.HashSet;
import java.util.Set;

public final class RelationChangeUtil {

    private RelationChangeUtil() {}

    public static <T> RelationChange<T> change(
            Set<T> existing,
            Set<T> incoming
    ) {
        Set<T> toDelete = new HashSet<>(existing);
        toDelete.removeAll(incoming);

        Set<T> toInsert = new HashSet<>(incoming);
        toInsert.removeAll(existing);

        return RelationChange.of(toInsert, toDelete);
    }
}
