package teamdevhub.devhub.adapter.out.common.util;

import teamdevhub.devhub.adapter.out.common.entity.RelationDiff;

import java.util.HashSet;
import java.util.Set;

public final class RelationDiffUtil {

    private RelationDiffUtil() {}

    public static <T> RelationDiff<T> diff(
            Set<T> existing,
            Set<T> incoming
    ) {
        Set<T> toDelete = new HashSet<>(existing);
        toDelete.removeAll(incoming);

        Set<T> toInsert = new HashSet<>(incoming);
        toInsert.removeAll(existing);

        return RelationDiff.of(toInsert, toDelete);
    }
}
