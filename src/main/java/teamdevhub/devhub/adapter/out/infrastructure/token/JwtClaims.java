package teamdevhub.devhub.adapter.out.infrastructure.token;

public final class JwtClaims {

    private JwtClaims() {}

    public static final String EMAIL = "email";

    public static final String USER_ROLE = "user_role";
    public static final String TOKEN_TYPE = "token_type";

    public static final String SIGNUP_STATUS = "signup_status"; // optional
    public static final String OAUTH_PROVIDER = "oauth_provider";
}