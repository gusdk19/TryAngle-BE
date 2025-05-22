package capstone.TryAngle.repository;

import capstone.TryAngle.model.user.Badge;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.model.user.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Integer> {
    boolean existsByUserAndBadge(User user, Badge badge);
    List<UserBadge> findByUser(User user);

}
