package teamdevhub.devhub.core.common.provider;

public interface EncodedPasswordProvider {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}