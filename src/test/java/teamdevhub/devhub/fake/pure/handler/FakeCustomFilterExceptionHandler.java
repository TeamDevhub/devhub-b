package teamdevhub.devhub.fake.pure.handler;

import jakarta.servlet.http.HttpServletResponse;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.web.security.filter.CustomFilterExceptionHandler;

public class FakeCustomFilterExceptionHandler extends CustomFilterExceptionHandler {

    private boolean handled = false;

    @Override
    public void handle(HttpServletResponse response, ErrorCode errorCode) {
        this.handled = true;
    }

    public boolean isHandled() {
        return handled;
    }
}