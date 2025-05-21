package capstone.TryAngle.repository;

import capstone.TryAngle.model.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

}
