package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.ChallengeResponseDTO;

import java.util.List;

public interface ChallengeService {
    List<ChallengeResponseDTO.getChallengeDTO> getChallenges();
}
