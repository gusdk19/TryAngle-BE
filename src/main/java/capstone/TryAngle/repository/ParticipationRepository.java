package capstone.TryAngle.repository;

import capstone.TryAngle.model.challenge.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {

    List<Participation> findAllByUserUserId(Integer userId);

    Participation findByUserUserIdAndChallengeChallengeId(Integer userId, Integer challengeId);

    boolean existsByUserUserIdAndChallengeChallengeId(Integer userId, Integer challengeId);

    List<Participation> findAllByChallengeChallengeId(Integer challengeId);

    int countByChallengeChallengeId(Integer challengeId);

    @Query("SELECT DISTINCT p FROM Participation p " +
            "JOIN FETCH p.challenge c " +
            "LEFT JOIN FETCH p.authList a")
    List<Participation> findAllWithChallengeAndAuth();

}
