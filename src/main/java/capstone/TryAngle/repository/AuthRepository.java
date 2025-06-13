package capstone.TryAngle.repository;

import capstone.TryAngle.model.challenge.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuthRepository extends JpaRepository<Auth, Integer> {
    boolean existsByParticipationParticipationIdAndCreatedAtBetween(Integer participationId, LocalDateTime start, LocalDateTime end);
    List<Auth> findByParticipationParticipationIdAndCreatedAtBetween(Integer participationId, LocalDateTime start, LocalDateTime end);

    List<Auth> findAllByParticipationChallengeChallengeId(Integer challengeId);

    List<Auth> findByParticipationChallengeChallengeIdAndCreatedAtBetween(Integer challengeId, LocalDateTime todayStart, LocalDateTime todayEnd);

    @Query("SELECT a FROM Auth a " +
            "WHERE a.participation.user.userId = :userId " +
            "AND a.createdAt BETWEEN :start AND :end")
    List<Auth> findAllByUserAndAuthSuccessTrueAndCreatedAtBetween(
            @Param("userId") Integer userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
