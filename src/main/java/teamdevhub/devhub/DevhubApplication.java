package teamdevhub.devhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAwareProvider")
public class DevhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevhubApplication.class, args);
	}

}
