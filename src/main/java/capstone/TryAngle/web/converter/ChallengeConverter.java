package capstone.TryAngle.web.converter;

import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ChallengeConverter {
    public static List<ChallengeResponseDTO.toChallengeDTO> toChallengeListDTO(List<Challenge> challenges) {
        return challenges.stream()
                .map(challenge -> new ChallengeResponseDTO.toChallengeDTO(
                        challenge.getChallengeId(),
                        challenge.getChallengeName(),
                        challenge.getChallengeThumbnail(),
                        challenge.getChallengeShortIntro(),
                        challenge.getCategory(),
                        challenge.getChallengePublic(),
                        challenge.getStartDate(),
                        challenge.getEndDate(),
                        challenge.getAuthTimeStart(),
                        challenge.getAuthTimeEnd(),
                        challenge.getMaxPeople(),
                        challenge.getNowPeople(),
                        challenge.getMinDeposit(),
                        challenge.getReturnType(),
                        challenge.getAuthFrequency(),
                        challenge.getLeader().getNickname()
                ))
                .collect(Collectors.toList());
    }
}
