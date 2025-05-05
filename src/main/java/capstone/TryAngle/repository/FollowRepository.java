package capstone.TryAngle.repository;

import capstone.TryAngle.model.user.Follow;
import capstone.TryAngle.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    int countByFollowee(User user);

    int countByFollower(User user);
}
