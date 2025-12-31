package teamdevhub.devhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@SpringBootApplication
public class DevhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevhubApplication.class, args);
	}

}
