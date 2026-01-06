package teamdevhub.devhub.adapter.in.common.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.PARAMETER)  // 컨트롤러 메서드 파라미터에만 사용 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
public @interface CurrentUser {
}
