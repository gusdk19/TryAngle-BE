package capstone.TryAngle.repository;

import capstone.TryAngle.model.user.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Integer> {
}
