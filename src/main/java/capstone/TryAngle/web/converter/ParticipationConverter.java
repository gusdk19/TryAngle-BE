package capstone.TryAngle.web.converter;

import capstone.TryAngle.model.challenge.Authentication;
import capstone.TryAngle.model.challenge.Challenge;
import capstone.TryAngle.model.challenge.Participation;
import capstone.TryAngle.model.challenge.Vote;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import capstone.TryAngle.web.dto.ParticipationResponseDTO;

public class ParticipationConverter {

    public static ParticipationResponseDTO.getParticipationDTO toParticipationDTO(Participation participation, boolean authSuccess, boolean authVoteSuccess) {
        return new ParticipationResponseDTO.getParticipationDTO(
               participation.getChallenge().getChallengeId(),
                participation.getStatus(),
                participation.getParticipationSuccess(),
                participation.getDepositAmount(),
                participation.getDepositStatus(),
                participation.getCreatedAt(),
                authSuccess,
                authVoteSuccess
        );

    }

}
