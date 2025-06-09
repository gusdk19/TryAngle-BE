package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.ChallengeRequestDTO;
import capstone.TryAngle.web.dto.ChallengeResponseDTO;
import capstone.TryAngle.web.dto.ParticipationResponseDTO;

import java.util.List;

public interface ChallengeService {
    List<ChallengeResponseDTO.getChallengeDTO> getChallenges();

    ChallengeResponseDTO.getChallengeDTO getChallengeById(Integer challengeId);

    List<ChallengeResponseDTO.getMyChallengeDTO> getMyChallenges(String email);

    ParticipationResponseDTO.getParticipationDTO getMyChallengeStatus(String email, Integer challengeId);

    void deleteChallenge(Integer challengeId, String email);

    void createChallenge(ChallengeRequestDTO.createChallengeDTO createChallengeDTO, String email, Integer deposit);

    void updateChallenge(Integer challengeId, ChallengeRequestDTO.createChallengeDTO createChallengeDTO, String email);

    void joinChallenge(Integer challengeId, Integer deposit, String inviteCode, String email);

    void endChallenge(Integer challengeId);

    ChallengeResponseDTO.createInviteCodeDTO createInviteCode(String inviteCode, Integer challengeId, String email);

    void quitChallenge(Integer challengeId, String email);

    List<ChallengeResponseDTO.ChallengeDepositStatusDTO> getMyDepositStatus(String email);
}
