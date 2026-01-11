package teamdevhub.devhub.medium.common.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import teamdevhub.devhub.adapter.in.web.resolver.LoginUserArgumentResolver;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebConfigTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LoginUserArgumentResolver loginUserArgumentResolver;

    @Test
    @DisplayName("LoginUserArgumentResolver_가_등록된다")
    void createLoginUserArgumentResolver() {
        // given
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);

        // when
        List<HandlerMethodArgumentResolver> resolvers = requestMappingHandlerAdapter.getArgumentResolvers();

        // then
        assertThat(resolvers).contains(loginUserArgumentResolver);
    }
    @Test
    @DisplayName("corsMapping_적용을_확인할_수_있다")
    void verifyCorsMappingApplied() throws Exception {
        // given
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        // when
        mockMvc.perform(options("/any-path")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET"))
                // then
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }
}