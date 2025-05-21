package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import capstone.TryAngle.web.dto.ParticipationResponseDTO;

import java.util.List;

public interface ChallengeService {
    List<ChallengeResponseDTO.getChallengeDTO> getChallenges();

    ChallengeResponseDTO.getChallengeDTO getChallengeById(Integer challengeId);

    List<ChallengeResponseDTO.getChallengeDTO> getMyChallenges(String email);

    ParticipationResponseDTO.getParticipationDTO getMyChallengeStatus(String email, Integer challengeId);
}
