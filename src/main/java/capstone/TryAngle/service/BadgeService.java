package capstone.TryAngle.service;

import capstone.TryAngle.web.dto.UserResponseDTO;

import java.util.List;

public interface BadgeService {
    void getBadge(String email, Integer badgeId);
    List<UserResponseDTO.UserBadgesDTO> userBadges(String email);
}
