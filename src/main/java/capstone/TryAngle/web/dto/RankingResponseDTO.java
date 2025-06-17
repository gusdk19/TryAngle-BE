package capstone.TryAngle.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingResponseDTO {
    private Integer userId;
    private String nickname;
    private double successRate;
    private Integer challengeCount;
    private String profileImage;
    private String description;
}
