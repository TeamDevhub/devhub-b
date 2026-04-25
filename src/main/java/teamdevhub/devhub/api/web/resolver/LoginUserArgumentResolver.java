package teamdevhub.devhub.api.web.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.outbound.common.exception.AuthRuleException;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.core.auth.domain.UserCredential;

import java.util.Optional;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class) && methodParameter.getParameterType().equals(UserCredential.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {

        Object principal = Optional
                .ofNullable(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                )
                .map(Authentication::getPrincipal)
                .orElseThrow(
                        () -> AuthRuleException.of(ErrorCode.USER_NOT_FOUND));

        if (principal instanceof UserAuthentication userAuthentication) {
            return userAuthentication.getUser();
        }

        if (principal instanceof UserCredential userCredential) {
            return userCredential;
        }

        throw AuthRuleException.of(ErrorCode.USER_NOT_FOUND);
    }
}
