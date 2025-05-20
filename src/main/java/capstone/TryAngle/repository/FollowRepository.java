package capstone.TryAngle.repository;

import capstone.TryAngle.model.user.Follow;
import capstone.TryAngle.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    int countByFollowee(User user);
    int countByFollower(User user);

    @Query("SELECT f.followee FROM Follow f WHERE f.follower = :follower")
    List<User> findFolloweesByFollower(@Param("follower") User follower);

    @Query("SELECT f.follower FROM Follow f WHERE f.followee = :followee")
    List<User> findFollowersByFollowee(@Param("followee") User followee);

    boolean existsByFollowerAndFollowee(User follower, User followee);

    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);

    @Query("SELECT f.followee.userId FROM Follow f WHERE f.follower = :follower")
    List<Integer> findFollowingIdsByFollower(User follower);
}
