package capstone.TryAngle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@ComponentScan(basePackages = {"capstone.TryAngle", "capstone"})
public class TryAngleApplication {
	public static void main(String[] args) {
		SpringApplication.run(TryAngleApplication.class, args);
	}

}
