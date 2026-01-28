package teamdevhub.devhub.core.provider;

public interface EncodedPasswordProvider {

    String encode(String rawPassword);
    boolean matches(String rawPassword, String hashedPassword);
}