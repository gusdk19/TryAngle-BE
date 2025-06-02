package capstone.TryAngle.repository;

import capstone.TryAngle.model.challenge.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    int countByAuthAuthenticationIdAndCreatedAtBetween(Integer authId, LocalDateTime start, LocalDateTime end);
}
