package teamdevhub.devhub.port.in.common.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageCommand {

    private final int page;

    private final int size;

    private static final int MAX_SIZE = 100;

    public static PageCommand of(int page, int size) {
        int safeSize = Math.max(1, Math.min(size, MAX_SIZE));
        int safePage = Math.max(page, 0);
        return new PageCommand(safePage, safeSize);
    }
}