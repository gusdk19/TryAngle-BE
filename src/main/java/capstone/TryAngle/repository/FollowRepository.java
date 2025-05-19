package capstone.TryAngle.repository;

import capstone.TryAngle.model.user.Follow;
import capstone.TryAngle.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    int countByFollowee(User user);
    int countByFollower(User user);

    @Query("SELECT f.followee FROM Follow f WHERE f.follower = :follower")
    List<User> findFolloweesByFollower(@Param("follower") User follower);
}
