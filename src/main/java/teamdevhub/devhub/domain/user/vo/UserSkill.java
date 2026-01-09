package teamdevhub.devhub.domain.user.vo;

public record UserSkill(String skillCode) {

    public UserSkill {
        if (skillCode == null || skillCode.isBlank()) {
            throw new IllegalArgumentException("skillCode must not be blank");
        }
    }
}
