package capstone.TryAngle.web.dto;

import capstone.TryAngle.model.challenge.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ChallengeResponseDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getChallengeDTO {

        private Integer challengeId;
        private String challengeName;
        private String challengeThumbnail;
        private String challengeShortIntro;
        private String challengeDescription;
        private Category category;
        private boolean challengePublic;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime authTimeStart;
        private LocalTime authTimeEnd;
        private Integer maxPeople;
        private Integer nowPeople;
        private Integer minDeposit;
        private boolean returnType;
        private String authFrequency;
        private String depositManageMethod;
        private String authMethod;
        private String voteMethod;
        private String leaderNickname;
        private List<Integer> participantList;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class getMyChallengeDTO {

        private Integer challengeId;
        private String challengeName;
        private String challengeThumbnail;
        private String challengeShortIntro;
        private Category category;
        private boolean challengePublic;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime authTimeStart;
        private LocalTime authTimeEnd;
        private Integer maxPeople;
        private Integer nowPeople;
        private Integer minDeposit;
        private boolean returnType;
        private String authFrequency;
        private String leaderNickname;
        private boolean participationSuccess;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class createInviteCodeDTO{
        private String inviteCode;
    }

}
