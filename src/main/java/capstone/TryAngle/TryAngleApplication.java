package capstone.TryAngle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 스프링 시큐리티 잠시 꺼둠
@SpringBootApplication(exclude= SecurityAutoConfiguration.class)
@EnableJpaAuditing
@ComponentScan(basePackages = {"capstone.TryAngle", "capstone"})
public class TryAngleApplication {
	public static void main(String[] args) {
		SpringApplication.run(TryAngleApplication.class, args);
	}

}
