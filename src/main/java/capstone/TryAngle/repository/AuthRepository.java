package capstone.TryAngle.repository;

import capstone.TryAngle.model.challenge.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuthRepository extends JpaRepository<Auth, Integer> {
    boolean existsByParticipationParticipationIdAndCreatedAtBetween(Integer participationId, LocalDateTime start, LocalDateTime end);
    List<Auth> findByParticipationParticipationIdAndCreatedAtBetween(Integer participationId, LocalDateTime start, LocalDateTime end);

    List<Auth> findAllByParticipationChallengeChallengeId(Integer challengeId);
}
