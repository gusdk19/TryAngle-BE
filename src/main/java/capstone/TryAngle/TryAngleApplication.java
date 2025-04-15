package capstone.TryAngle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TryAngleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TryAngleApplication.class, args);
	}

}
