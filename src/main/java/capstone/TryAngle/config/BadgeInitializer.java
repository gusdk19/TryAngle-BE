package capstone.TryAngle.config;

import capstone.TryAngle.repository.BadgeRepository;
import capstone.TryAngle.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeInitializer implements CommandLineRunner {
    private final BadgeRepository badgeRepository;
    private final BadgeService badgeService;

    @Override
    public void run(String... args) throws Exception {
        if (badgeRepository.count() == 0) {
            badgeService.initializeBadges();
        }
    }
}
