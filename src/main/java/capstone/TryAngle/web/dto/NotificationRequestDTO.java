package capstone.TryAngle.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowNotificationDTO {
        private Integer followerId;
        private Integer followeeId;
    }
}
