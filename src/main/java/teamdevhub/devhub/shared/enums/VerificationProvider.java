package teamdevhub.devhub.shared.enums;

public enum VerificationProvider {
    EMAIL,
    GOOGLE,
    GITHUB,
    KAKAO,
    NAVER;

    public static VerificationProvider from(String provider) {
        return VerificationProvider.valueOf(provider.toUpperCase());
    }
}
