package capstone.TryAngle.repository;

import capstone.TryAngle.model.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

    List<Challenge> findAllByEndDate(LocalDate now);
}
