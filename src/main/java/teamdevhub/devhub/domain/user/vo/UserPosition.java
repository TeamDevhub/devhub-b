package teamdevhub.devhub.domain.user.vo;

public record UserPosition(String positionCode) {

    public UserPosition {
        if (positionCode == null || positionCode.isBlank()) {
            throw new IllegalArgumentException("positionCode must not be blank");
        }
    }
}
