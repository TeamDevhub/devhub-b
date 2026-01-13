package teamdevhub.devhub.domain.user.vo;

import java.util.Set;

public record UserPositionChangeResult(
        boolean changed,
        Set<UserPosition> previousPositions,
        Set<UserPosition> currentPositions
) {
    public static UserPositionChangeResult changed(Set<UserPosition> previous, Set<UserPosition> current) {
        return new UserPositionChangeResult(true, Set.copyOf(previous), Set.copyOf(current));
    }

    public static UserPositionChangeResult unchanged(Set<UserPosition> current) {
        return new UserPositionChangeResult(false, Set.copyOf(current), Set.copyOf(current));
    }
}
