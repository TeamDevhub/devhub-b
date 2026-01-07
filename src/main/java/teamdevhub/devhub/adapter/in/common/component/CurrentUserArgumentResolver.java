package teamdevhub.devhub.adapter.in.common.component;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import teamdevhub.devhub.adapter.in.common.annotation.LoginUser;
import teamdevhub.devhub.adapter.out.auth.userDetail.LoginAuthentication;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;

import java.util.Optional;

@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class) &&
                methodParameter.getParameterType().equals(AuthenticatedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {

        Object principal = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .orElseThrow(() -> new RuntimeException("No authenticated user found"));

        if (principal instanceof LoginAuthentication loginAuthentication) {
            return loginAuthentication.getUser();
        }

        if (principal instanceof AuthenticatedUser authenticatedUser) {
            return authenticatedUser;
        }

        throw new RuntimeException("No authenticated user found");
    }
}
