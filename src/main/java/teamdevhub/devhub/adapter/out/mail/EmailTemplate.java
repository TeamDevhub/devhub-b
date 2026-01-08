package teamdevhub.devhub.adapter.out.mail;

import java.util.Map;

public record EmailTemplate(
        String subject,
        String templatePath,
        Map<String, Object> variables
) {
}
