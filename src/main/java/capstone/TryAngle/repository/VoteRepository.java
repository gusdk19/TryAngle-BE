package capstone.TryAngle.repository;

import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.model.challenge.Vote;
import capstone.TryAngle.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    int countByAuthAuthenticationIdAndCreatedAtBetween(Integer authId, LocalDateTime start, LocalDateTime end);

    int countByAuth_AuthenticationId(Integer authId);

    int countByAuth_AuthenticationIdAndVoteTypeTrue(Integer authenticationId);

    boolean existsByUserUserIdAndAuthAuthenticationId(Integer userId, Integer authenticationId);

    int countByAuthAuthenticationIdAndVoteType(Integer authenticationId, boolean b);

    Optional<Vote> findByUserAndAuth(User user, Auth auth);

    boolean existsByAuthAuthenticationIdAndUserUserId(Integer authId, Integer userId);
}
