package capstone.TryAngle.service;

import capstone.TryAngle.common.GeneralException;
import capstone.TryAngle.common.status.ErrorStatus;
import capstone.TryAngle.model.challenge.Auth;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.challenge.Participation;
import capstone.TryAngle.model.user.User;
import capstone.TryAngle.repository.ParticipationRepository;
import capstone.TryAngle.repository.UserRepository;
import capstone.TryAngle.repository.FollowRepository;
import capstone.TryAngle.web.dto.RankingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public List<RankingResponseDTO> getAllRanking() {
        List<Participation> participations = participationRepository.findAllWithChallengeAndAuth();

        Map<User, List<Participation>> userMap = participations.stream()
                .collect(Collectors.groupingBy(Participation::getUser));

        List<RankingResponseDTO> result = new ArrayList<>();

        for (Map.Entry<User, List<Participation>> entry : userMap.entrySet()) {
            User user = entry.getKey();
            List<Participation> userParticipations = entry.getValue();

            int totalExpected = 0;
            int totalSuccess = 0;

            for (Participation p : userParticipations) {
                Challenge challenge = p.getChallenge();
                int expected = getExpectedCount(challenge, p);

                long success = p.getAuthList().stream()
                        .filter(Auth::getAuthSuccess)
                        .count();

                totalExpected += expected;
                totalSuccess += Math.min((int) success, expected);
            }

            double rate = (totalExpected > 0)
                    ? Math.round((totalSuccess * 1000.0 / totalExpected)) / 10.0
                    : 0.0;

            result.add(new RankingResponseDTO(
                    user.getUserId(),
                    user.getNickname(),
                    rate,
                    userParticipations.size()
            ));
        }

        return result.stream()
                .sorted(Comparator
                        .comparingDouble(RankingResponseDTO::getSuccessRate).reversed()
                        .thenComparingInt(RankingResponseDTO::getChallengeCount).reversed()
                )
                .collect(Collectors.toList());

    }

    public List<RankingResponseDTO> getFollowingRanking(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<User> following = followRepository.findFolloweesByFollower(user);
        List<RankingResponseDTO> all = getAllRanking();

        return all.stream()
                .filter(dto -> following.stream().anyMatch(f -> f.getUserId().equals(dto.getUserId())))
                .toList();
    }

    private int getExpectedCount(Challenge challenge, Participation participation) {
        LocalDate from = participation.getCreatedAt().toLocalDate();
        LocalDate to = challenge.getEndDate().isBefore(LocalDate.now()) ? challenge.getEndDate() : LocalDate.now();

        long days = from.datesUntil(to.plusDays(1)).count();
        double ratio = getWeeklyRatio(challenge.getAuthFrequency());

        return (int) Math.round(days * ratio);
    }

    private double getWeeklyRatio(String freq) {
        return switch (freq.trim()) {
            case "매일" -> 1.0;
            case "평일 매일" -> 5.0 / 7.0;
            case "주말 매일" -> 2.0 / 7.0;
            case "주 1일" -> 1.0 / 7.0;
            case "주 2일" -> 2.0 / 7.0;
            case "주 3일" -> 3.0 / 7.0;
            case "주 4일" -> 4.0 / 7.0;
            case "주 5일" -> 5.0 / 7.0;
            case "주 6일" -> 6.0 / 7.0;
            default -> 0.0;
        };
    }
}