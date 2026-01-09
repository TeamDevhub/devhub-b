package teamdevhub.devhub.medium.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QueryDslConfigTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    void jpaQueryFactory_가_생성된다() {
        // given, when, then
        assertThat(jpaQueryFactory).isNotNull();
    }
}