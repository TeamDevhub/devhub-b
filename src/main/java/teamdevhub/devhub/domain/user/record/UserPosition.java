package teamdevhub.devhub.domain.user.record;

public record UserPosition(String positionCode) {

    public UserPosition {
        if (positionCode == null || positionCode.isBlank()) {
            throw new IllegalArgumentException("positionCode must not be blank");
        }
    }
}
