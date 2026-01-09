package teamdevhub.devhub.medium.common.config;

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
    void LoginUserArgumentResolver_가_등록된다() {
        RequestMappingHandlerAdapter adapter = context.getBean(RequestMappingHandlerAdapter.class);

        List<HandlerMethodArgumentResolver> resolvers = adapter.getArgumentResolvers();
        assertThat(resolvers).contains(loginUserArgumentResolver);
    }
    @Test
    void corsMapping_적용을_확인할_수_있다() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        mockMvc.perform(options("/any-path")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }
}