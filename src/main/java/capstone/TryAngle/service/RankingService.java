package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.RankingResponseDTO;

import java.util.List;

public interface RankingService {
    List<RankingResponseDTO> getAllRanking();
    List<RankingResponseDTO> getFollowingRanking(String email);
}
