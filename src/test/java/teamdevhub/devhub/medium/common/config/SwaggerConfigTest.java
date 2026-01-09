package teamdevhub.devhub.medium.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Autowired
    private GroupedOpenApi userGroup;

    @Test
    void openAPI_빈이_등록된다() {
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("DevHub API");
    }

    @Test
    void userGroup_빈이_등록된다() {
        assertThat(userGroup).isNotNull();
        assertThat(userGroup.getGroup()).isEqualTo("User");
    }
}
