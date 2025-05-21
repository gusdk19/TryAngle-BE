package capstone.TryAngle.repository;

import capstone.TryAngle.model.challenge.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {
    boolean existsByParticipationParticipationIdAndCreatedAtBetween(Integer participationId, LocalDateTime start, LocalDateTime end);
    List<Authentication> findByParticipationParticipationIdAndCreatedAtBetween(Integer participationId, LocalDateTime start, LocalDateTime end);
}
