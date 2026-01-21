package teamdevhub.devhub.fake.pure.handler;

import jakarta.servlet.http.HttpServletResponse;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.web.security.filter.CustomFilterExceptionHandler;

public class FakeCustomFilterExceptionHandler extends CustomFilterExceptionHandler {

    private boolean handled = false;
    private ErrorCode lastErrorCode = null;

    @Override
    public void handle(HttpServletResponse response, ErrorCode errorCode) {
        this.handled = true;
        this.lastErrorCode = errorCode;
    }

    public boolean isHandled() {
        return handled;
    }

    public ErrorCode getLastErrorCode() {
        return lastErrorCode;
    }

    public void reset() {
        this.handled = false;
        this.lastErrorCode = null;
    }
}